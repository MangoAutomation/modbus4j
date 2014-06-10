/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;

/**
 * @author Matthew Lohbihler
 */
public class ReadSerialTest {
    public static void main(String[] args) throws Exception {
        SerialParameters serialParameters = new SerialParameters();
        serialParameters.setCommPortId("COM1");
        serialParameters.setBaudRate(9600);

        ModbusMaster master = new ModbusFactory().createRtuMaster(serialParameters);
        master.setTimeout(200);
        master.setRetries(1);
        master.init();

        for (int i = 1; i < 5; i++) {
            long start = System.currentTimeMillis();
            System.out.print("Testing " + i + "... ");
            System.out.println(master.testSlaveNode(i));
            System.out.println("Time: " + (System.currentTimeMillis() - start));
        }

        // try {
        // System.out.println(master.send(new ReadHoldingRegistersRequest(1, 0, 1)));
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }

        // try {
        // // ReadCoilsRequest request = new ReadCoilsRequest(2, 65534, 1);
        // ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master
        // .send(new ReadHoldingRegistersRequest(2, 0, 1));
        // System.out.println(response);
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }

        // System.out.println(master.scanForSlaveNodes());

        master.destroy();
    }
}
