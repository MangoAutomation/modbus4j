package com.serotonin.modbus4j.ip.rtu;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.serial.rtu.RtuMessageParser;
import com.serotonin.modbus4j.serial.rtu.RtuMessageRequest;
import com.serotonin.modbus4j.serial.rtu.RtuMessageResponse;
import com.serotonin.modbus4j.sero.messaging.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Tcp2RtuMaster extends ModbusMaster {

    private final Log LOG = LogFactory.getLog(Tcp2RtuMaster.class);

    private final IpParameters ipParameters;
    private final boolean keepAlive;
    private Socket socket;
    private Transport transport;
    private MessageControl conn;

    public Tcp2RtuMaster(IpParameters params, boolean keepAlive) {
        this.ipParameters = params;
        this.keepAlive = keepAlive;
    }
    @Override
    protected MessageControl getMessageControl() {
        MessageControl messageControl = super.getMessageControl();
        messageControl.DEBUG = true;

        return messageControl;
    }
    @Override
    public void init()  throws ModbusInitException {
        try {
            if (this.keepAlive) {
                this.openConnection();
            }
        } catch (Exception var2) {
            throw new ModbusInitException(var2);
        }
        this.initialized = true;

    }

    public synchronized void destroy() {
        this.closeConnection();
        this.initialized = false;
    }

    @Override
    public ModbusResponse sendImpl(ModbusRequest request) throws ModbusTransportException {
        try {
            if (!this.keepAlive) {
                this.openConnection();
            }

            if (this.conn == null) {
                this.LOG.debug("Connection null: " + this.ipParameters.getPort());
            }
        } catch (Exception var18) {
            this.closeConnection();
            throw new ModbusTransportException(var18, request.getSlaveId());
        }

        RtuMessageRequest rtuRequest = new RtuMessageRequest(request);

        try {
            RtuMessageResponse rtuResponse = (RtuMessageResponse)this.conn.send(rtuRequest);
            ModbusResponse var4;
            if (rtuResponse == null) {
                var4 = null;
                return var4;
            } else {
                var4 = rtuResponse.getModbusResponse();
                return var4;
            }
        } catch (Exception var8) {
//            throw new ModbusTransportException(var8, request.getSlaveId());
            if (this.keepAlive) {

                try {
                    this.openConnection();
                    RtuMessageResponse rtuResponse = (RtuMessageResponse)this.conn.send(rtuRequest);
                    ModbusResponse var4;
                    if (rtuResponse == null) {
                        var4 = null;
                        return var4;
                    } else {
                        var4 = rtuResponse.getModbusResponse();
                        return var4;
                    }
                } catch (Exception e) {
                    throw new ModbusTransportException(e, request.getSlaveId());
                }

            }
            throw new ModbusTransportException(var8, request.getSlaveId());
        } finally {
            if (!this.keepAlive) {
                this.closeConnection();
            }
        }
    }
    private void closeConnection() {
        this.closeMessageControl(this.conn);

        try {
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException var2) {
            this.getExceptionHandler().receivedException(var2);
        }

        this.conn = null;
        this.socket = null;
    }
    private void openConnection() throws IOException {
        this.closeConnection();
        int retries = this.getRetries();
        int retryPause = 50;

        while(true) {
            try {
                this.socket = new Socket();
                this.socket.setSoTimeout(this.getTimeout());
                this.socket.connect(new InetSocketAddress(this.ipParameters.getHost(), this.ipParameters.getPort()), this.getTimeout());
                if (this.getePoll() != null) {
                    this.transport = new EpollStreamTransport(this.socket.getInputStream(), this.socket.getOutputStream(), this.getePoll());
                } else {
                    this.transport = new StreamTransport(this.socket.getInputStream(), this.socket.getOutputStream());
                }
                break;
            } catch (IOException var6) {
                this.closeConnection();
                if (retries <= 0) {
                    throw var6;
                }

                --retries;

                try {
                    Thread.sleep(retryPause);
                } catch (InterruptedException var5) {
                }

                retryPause *= 2;
                if (retryPause > 1000) {
                    retryPause = 1000;
                }
            }
        }
        RtuMessageParser rtuMessageParser = new RtuMessageParser(true);

        this.conn = this.getMessageControl();
        this.conn.start(this.transport, rtuMessageParser, (RequestHandler)null, new Tcp2RtuSerialWaitingRoomKeyFactory());
        if (this.getePoll() == null) {
            ((StreamTransport)this.transport).start("Modbus4J TcpMaster");
        }
    }

}
