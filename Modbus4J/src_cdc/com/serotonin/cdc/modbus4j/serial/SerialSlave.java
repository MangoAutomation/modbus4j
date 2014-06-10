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
package com.serotonin.cdc.modbus4j.serial;

import gnu.io.SerialPort;

import com.serotonin.cdc.io.serial.SerialParameters;
import com.serotonin.cdc.io.serial.SerialUtils;
import com.serotonin.cdc.messaging.StreamTransport;
import com.serotonin.cdc.modbus4j.ModbusSlaveSet;
import com.serotonin.cdc.modbus4j.exception.ModbusInitException;

abstract public class SerialSlave extends ModbusSlaveSet {
    // Configuration fields.
    private final SerialParameters serialParameters;

    // Runtime fields
    private SerialPort serialPort;
    protected StreamTransport transport;

    public SerialSlave(SerialParameters params) {
        serialParameters = params;
    }

    //Override
    public void start() throws ModbusInitException {
        try {
            serialPort = SerialUtils.openSerialPort(serialParameters);
            transport = new StreamTransport(serialPort.getInputStream(), serialPort.getOutputStream());
        }
        catch (Exception e) {
            throw new ModbusInitException(e);
        }
    }

    //Override
    public void stop() {
        SerialUtils.close(serialPort);
    }
}
