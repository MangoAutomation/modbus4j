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
package com.serotonin.cdc.modbus4j;

import com.serotonin.cdc.io.serial.SerialParameters;
import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.ModbusIdException;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.modbus4j.ip.IpParameters;
import com.serotonin.cdc.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.cdc.modbus4j.ip.tcp.TcpSlave;
import com.serotonin.cdc.modbus4j.ip.udp.UdpMaster;
import com.serotonin.cdc.modbus4j.ip.udp.UdpSlave;
import com.serotonin.cdc.modbus4j.msg.ModbusRequest;
import com.serotonin.cdc.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.cdc.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.cdc.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.cdc.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.cdc.modbus4j.serial.ascii.AsciiMaster;
import com.serotonin.cdc.modbus4j.serial.ascii.AsciiSlave;
import com.serotonin.cdc.modbus4j.serial.rtu.RtuMaster;
import com.serotonin.cdc.modbus4j.serial.rtu.RtuSlave;

public class ModbusFactory {
    //
    // Modbus masters
    //
    public ModbusMaster createRtuMaster(SerialParameters params, int concurrency) {
        return new RtuMaster(params, concurrency);
    }

    public ModbusMaster createAsciiMaster(SerialParameters params, int concurrency) {
        return new AsciiMaster(params, concurrency);
    }

    public ModbusMaster createTcpMaster(IpParameters params, boolean keepAlive) {
        return new TcpMaster(params, keepAlive);
    }

    public ModbusMaster createUdpMaster(IpParameters params) {
        return new UdpMaster(params);
    }

    //
    // Modbus slaves
    //
    public ModbusSlaveSet createRtuSlave(SerialParameters params) {
        return new RtuSlave(params);
    }

    public ModbusSlaveSet createAsciiSlave(SerialParameters params) {
        return new AsciiSlave(params);
    }

    public ModbusSlaveSet createTcpSlave(boolean encapsulated) {
        return new TcpSlave(encapsulated);
    }

    public ModbusSlaveSet createUdpSlave(boolean encapsulated) {
        return new UdpSlave(encapsulated);
    }

    //
    // Modbus requests
    //
    public ModbusRequest createReadRequest(int slaveId, int range, int offset, int length)
            throws ModbusTransportException, ModbusIdException {
        ModbusUtils.validateRegisterRange(range);

        if (range == RegisterRange.COIL_STATUS)
            return new ReadCoilsRequest(slaveId, offset, length);

        if (range == RegisterRange.INPUT_STATUS)
            return new ReadDiscreteInputsRequest(slaveId, offset, length);

        if (range == RegisterRange.INPUT_REGISTER)
            return new ReadInputRegistersRequest(slaveId, offset, length);

        return new ReadHoldingRegistersRequest(slaveId, offset, length);
    }
}
