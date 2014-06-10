/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * @author Matthew Lohbihler
 */
public class Test3 {
    public static void main(String[] args) throws Exception {
        SerialParameters params = new SerialParameters();
        params.setCommPortId("COM1");

        ModbusMaster master = new ModbusFactory().createRtuMaster(params);
        master.init();

        System.out.println(master.testSlaveNode(5));

        // Define the point locator.
        BaseLocator<Number> loc = BaseLocator.holdingRegister(1, 0, DataType.TWO_BYTE_INT_UNSIGNED);

        // Set the point value
        master.setValue(loc, 1800);

        // Get the point value
        System.out.println(master.getValue(loc));
    }
}
