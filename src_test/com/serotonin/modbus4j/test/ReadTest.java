/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.NumericLocator;

/**
 * @author Matthew Lohbihler
 */
public class ReadTest {
    public static void main(String[] args) throws Exception {
        IpParameters ipParameters = new IpParameters();
        // ipParameters.setHost("99.247.60.96");
        // ipParameters.setHost("193.109.41.121");
        //      ipParameters.setHost("10.241.224.195");
        ipParameters.setHost("172.19.40.10");
        ipParameters.setPort(502);
        ipParameters.setEncapsulated(false);

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, true);
        ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);
        master.setTimeout(8000);
        master.setRetries(0);
        master.init();

        //        for (int i = 1; i < 5; i++) {
        //            System.out.print("Testing " + i + "... ");
        //            System.out.println(master.testSlaveNode(i));
        //        }

        NumericLocator el = new NumericLocator(1, RegisterRange.HOLDING_REGISTER, 22, DataType.FOUR_BYTE_FLOAT);
        NumericLocator fjk = new NumericLocator(1, RegisterRange.HOLDING_REGISTER, 36, DataType.FOUR_BYTE_FLOAT);

        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("el: " + master.getValue(el));
                System.out.println("fjk: " + master.getValue(fjk));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

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
