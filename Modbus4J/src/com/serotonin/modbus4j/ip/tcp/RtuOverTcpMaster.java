package com.serotonin.modbus4j.ip.tcp;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;

import java.io.IOException;
import java.net.*;

public class RtuOverTcpMaster extends ModbusMaster {

    private final InetSocketAddress inetSocketAddress;
    private final Options options;
    private final Socket tcpClient;

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress){
        this(inetSocketAddress,new Options());
    }

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress,final Options options){
        this.inetSocketAddress=inetSocketAddress;
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
            this.tcpClient.connect(inetSocketAddress);
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
        return null;
    }

    public static class Options{

        public boolean keepAlive=true;

    }

}
