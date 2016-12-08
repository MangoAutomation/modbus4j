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
package com.serotonin.modbus4j.ip.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.BaseMessageParser;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpMessageResponse;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.encap.EncapMessageParser;
import com.serotonin.modbus4j.ip.encap.EncapMessageRequest;
import com.serotonin.modbus4j.ip.xa.XaMessageParser;
import com.serotonin.modbus4j.ip.xa.XaMessageRequest;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.sero.messaging.OutgoingRequestMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class UdpMaster extends ModbusMaster {
    private static final int MESSAGE_LENGTH = 1024;

    private short nextTransactionId = 0;
    private final IpParameters ipParameters;

    // Runtime fields.
    private BaseMessageParser messageParser;
    private DatagramSocket socket;

    public UdpMaster(IpParameters params) {
        ipParameters = params;
    }

    protected short getNextTransactionId() {
        return nextTransactionId++;
    }

    @Override
    public void init() throws ModbusInitException {
        if (ipParameters.isEncapsulated())
            messageParser = new EncapMessageParser(true);
        else
            messageParser = new XaMessageParser(true);

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(getTimeout());
        }
        catch (SocketException e) {
            throw new ModbusInitException(e);
        }
        initialized = true;
    }

    @Override
    public void destroy() {
        socket.close();
        initialized = false;
    }

    @Override
    public ModbusResponse sendImpl(ModbusRequest request) throws ModbusTransportException {
        // Wrap the modbus request in an ip request.
        OutgoingRequestMessage ipRequest;
        if (ipParameters.isEncapsulated())
            ipRequest = new EncapMessageRequest(request);
        else
            ipRequest = new XaMessageRequest(request, getNextTransactionId());

        IpMessageResponse ipResponse;

        try {
            int attempts = getRetries() + 1;

            while (true) {
                // Send the request.
                sendImpl(ipRequest);

                if (!ipRequest.expectsResponse())
                    return null;

                // Receive the response.
                try {
                    ipResponse = receiveImpl();
                }
                catch (SocketTimeoutException e) {
                    attempts--;
                    if (attempts > 0)
                        // Try again.
                        continue;

                    throw new ModbusTransportException(e, request.getSlaveId());
                }

                // We got the response
                break;
            }

            return ipResponse.getModbusResponse();
        }
        catch (IOException e) {
            throw new ModbusTransportException(e, request.getSlaveId());
        }
    }

    private void sendImpl(OutgoingRequestMessage request) throws IOException {
        byte[] data = request.getMessageData();
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ipParameters.getHost()),
                ipParameters.getPort());
        socket.send(packet);
    }

    private IpMessageResponse receiveImpl() throws IOException, ModbusTransportException {
        DatagramPacket packet = new DatagramPacket(new byte[MESSAGE_LENGTH], MESSAGE_LENGTH);
        socket.receive(packet);

        // We could verify that the packet was received from the same address to which the request was sent,
        // but let's not bother with that yet.

        ByteQueue queue = new ByteQueue(packet.getData(), 0, packet.getLength());
        IpMessageResponse response;
        try {
            response = (IpMessageResponse) messageParser.parseMessage(queue);
        }
        catch (Exception e) {
            throw new ModbusTransportException(e);
        }

        if (response == null)
            throw new ModbusTransportException("Invalid response received");

        return response;
    }
}
