/*
 * Copyright (C) 2023 Radix IoT LLC. All rights reserved.
 *
 *
 */

package com.serotonin.modbus4j.test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;

/**
 * Prove that we can connect any number of clients to a Modbus Slave
 */
public class MultipleTcpConnectionsTest {

    private static final int maxConnections = 10;
    private static final int slaveId = 1;
    private static final int numRegisters = 10;

    public static void main(String[] args) throws ModbusInitException, InterruptedException {
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost("localhost");

        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusSlaveSet slave = modbusFactory.createTcpSlave(false);

        //Add process image
        BasicProcessImage image = new BasicProcessImage(slaveId);
        for(int i=0; i<numRegisters; i++) {
            image.setHoldingRegister(i, (short)i);
        }
        slave.addProcessImage(image);

        //Start the slave in a new thread
        new Thread() {
            @Override
            public void run() {
                try {
                    slave.start();
                } catch (ModbusInitException e) {
                    throw new RuntimeException(e);
                } finally {
                    slave.stop();
                }
            }
        }.start();


        //Kick off a Thread to open connections and make a request
        AtomicInteger count = new AtomicInteger();
        while(count.get() <  maxConnections) {
            new Thread() {
                int number = count.getAndIncrement();
                @Override
                public void run() {
                    System.out.println("Starting connection " + number);
                    ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);
                    try {
                        master.init();
                        System.out.println("Connection " + number + " requesting data");
                        readHoldingRegistersTest(master, slaveId, 0, 10, number);
                    } catch (ModbusInitException e) {
                        System.out.println("Failed on connection " + number);
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } finally {
                        master.destroy();
                    }
                }
            }.start();
        }

        //Sleep here and wait for connections
        Thread.sleep(5000);

    }

    public static void readHoldingRegistersTest(ModbusMaster master, int slaveId, int start, int len, int connectionNumber) {
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);

            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else
                System.out.println("Connection " + connectionNumber + " Recieved: " + Arrays.toString(response.getShortData()));
        }
        catch (ModbusTransportException e) {
            e.printStackTrace();
        }
    }

}
