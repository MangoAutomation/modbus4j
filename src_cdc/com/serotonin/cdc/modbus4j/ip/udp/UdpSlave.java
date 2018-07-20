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
package com.serotonin.cdc.modbus4j.ip.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.serotonin.cdc.messaging.IncomingMessage;
import com.serotonin.cdc.messaging.IncomingRequestMessage;
import com.serotonin.cdc.messaging.OutgoingResponseMessage;
import com.serotonin.cdc.modbus4j.ModbusSlaveSet;
import com.serotonin.cdc.modbus4j.base.BaseMessageParser;
import com.serotonin.cdc.modbus4j.base.BaseRequestHandler;
import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.exception.ModbusInitException;
import com.serotonin.cdc.modbus4j.ip.encap.EncapMessageParser;
import com.serotonin.cdc.modbus4j.ip.encap.EncapRequestHandler;
import com.serotonin.cdc.modbus4j.ip.xa.XaMessageParser;
import com.serotonin.cdc.modbus4j.ip.xa.XaRequestHandler;
import com.serotonin.cdc.util.queue.ByteQueue;

public class UdpSlave extends ModbusSlaveSet {
    // Configuration fields
    private final int port;

    // Runtime fields.
    DatagramSocket datagramSocket;
    private final ExecutorService executorService;
    final BaseMessageParser messageParser;
    final BaseRequestHandler requestHandler;

    public UdpSlave(boolean encapsulated) {
        this(ModbusUtils.TCP_PORT, encapsulated);
    }

    public UdpSlave(int port, boolean encapsulated) {
        this.port = port;

        if (encapsulated) {
            messageParser = new EncapMessageParser(false);
            requestHandler = new EncapRequestHandler(this);
        }
        else {
            messageParser = new XaMessageParser(false);
            requestHandler = new XaRequestHandler(this);
        }

        executorService = Executors.newCachedThreadPool();
    }

    //Override
    public void start() throws ModbusInitException {
        try {
            datagramSocket = new DatagramSocket(port);

            DatagramPacket datagramPacket;
            while (true) {
                datagramPacket = new DatagramPacket(new byte[1028], 1028);
                datagramSocket.receive(datagramPacket);

                UdpConnectionHandler handler = new UdpConnectionHandler(datagramPacket);
                executorService.execute(handler);
            }
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    //Override
    public void stop() {
        // Close the socket first to prevent new messages.
        datagramSocket.close();

        // Close the executor service.
        executorService.shutdown();
        try {
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            getExceptionHandler().receivedException(e);
        }
    }

    // int getSlaveId() {
    // return slaveId;
    // }
    //
    // ProcessImage getProcessImage() {
    // return processImage;
    // }

    class UdpConnectionHandler implements Runnable {
        private final DatagramPacket requestPacket;

        UdpConnectionHandler(DatagramPacket requestPacket) {
            this.requestPacket = requestPacket;
        }

        public void run() {
            try {
                ByteQueue requestQueue = new ByteQueue(requestPacket.getData(), 0, requestPacket.getLength());

                // Parse the request data and get the response.
                IncomingMessage request = messageParser.parseMessage(requestQueue);
                OutgoingResponseMessage response = requestHandler.handleRequest((IncomingRequestMessage) request);

                if (response == null)
                    return;

                // Create a response packet.
                byte[] responseData = response.getMessageData();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        requestPacket.getAddress(), requestPacket.getPort());

                // Send the response back.
                datagramSocket.send(responsePacket);
            }
            catch (Exception e) {
                getExceptionHandler().receivedException(e);
            }
        }
    }
}
