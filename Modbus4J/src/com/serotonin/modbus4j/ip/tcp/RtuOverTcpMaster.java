package com.serotonin.modbus4j.ip.tcp;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.serial.rtu.RtuMessageRequest;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class RtuOverTcpMaster extends ModbusMaster {

    private InetSocketAddress inetSocketAddress;
    private final Options options;
    private final Socket tcpClient;

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress){
        this(inetSocketAddress,new Options());
    }

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress, final Options options){
        this.inetSocketAddress = inetSocketAddress;
        this.options=new Options();
        this.tcpClient=new Socket();
        try {
            this.tcpClient.setKeepAlive(options.keepAlive);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void init() throws ModbusInitException {
        try {
            this.tcpClient.connect(this.inetSocketAddress);
            this.initialized=true;
        } catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    @Override
    public synchronized void destroy() {
        try {
            if(!this.tcpClient.isClosed()) {
                this.tcpClient.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized ModbusResponse sendImpl(final ModbusRequest request) throws ModbusTransportException {
        final RtuMessageRequest rtuMessageRequest=new RtuMessageRequest(request);
        try {
            final OutputStream outputStream=this.tcpClient.getOutputStream();
            outputStream.write(rtuMessageRequest.getMessageData());

            final InputStream inputStream=this.tcpClient.getInputStream();
            final ByteQueue byteQueue=new ByteQueue();
            byteQueue.read(inputStream,inputStream.available());
            return ModbusResponse.createModbusResponse(byteQueue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Options{

        public boolean keepAlive=true;

    }

}
