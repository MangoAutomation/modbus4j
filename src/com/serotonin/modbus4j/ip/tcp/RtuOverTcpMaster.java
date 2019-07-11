/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j.ip.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.serial.rtu.RtuMessageRequest;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * <p>RtuOverTcpMaster class.</p>
 *
 * @version 5.0.1
 */
public class RtuOverTcpMaster extends ModbusMaster {

    private final InetSocketAddress inetSocketAddress;
    private final Socket tcpClient;
    private final Options options;

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress){
        this(inetSocketAddress, new Options());
    }

    public RtuOverTcpMaster(final InetSocketAddress inetSocketAddress, final Options options){
        this.inetSocketAddress = inetSocketAddress;
        this.options = options;
        this.tcpClient = new Socket();
        
        try {
            this.tcpClient.setKeepAlive(this.options.keepAlive);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void init() throws ModbusInitException {
        try {
            this.tcpClient.connect(this.inetSocketAddress);
            this.initialized = true;
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
        final RtuMessageRequest rtuMessageRequest = new RtuMessageRequest(request);
        try {
            final OutputStream outputStream = this.tcpClient.getOutputStream();
            outputStream.write(rtuMessageRequest.getMessageData());

            final InputStream inputStream = this.tcpClient.getInputStream();

            Thread.sleep(this.options.waitTime);
            
            final ByteQueue byteQueue = new ByteQueue();
            byteQueue.read(inputStream, inputStream.available());
            
            return ModbusResponse.createModbusResponse(byteQueue);
        } catch (IOException | InterruptedException e) {
            throw new ModbusTransportException(e);
        }
    }

    public static class Options {
        private boolean keepAlive = false;
        private int waitTime = 100;
        public boolean isKeepAlive() {
            return keepAlive;
        }
        public void setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
        }
        public int getWaitTime() {
            return waitTime;
        }
        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }
    }

}
