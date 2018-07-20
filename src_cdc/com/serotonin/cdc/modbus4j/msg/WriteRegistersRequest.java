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
package com.serotonin.cdc.modbus4j.msg;

import com.serotonin.cdc.modbus4j.Modbus;
import com.serotonin.cdc.modbus4j.ProcessImage;
import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.code.FunctionCode;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.util.queue.ByteQueue;

public class WriteRegistersRequest extends ModbusRequest {
    private int startOffset;
    private byte[] data;

    public WriteRegistersRequest(int slaveId, int startOffset, short[] sdata) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        data = convertToBytes(sdata);
    }

    //Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        ModbusUtils.validateOffset(startOffset);
        int registerCount = data.length / 2;
        if (registerCount < 1 || registerCount > modbus.getMaxWriteRegisterCount())
            throw new ModbusTransportException("Invalid number of registers: " + registerCount, slaveId);
        ModbusUtils.validateEndOffset(startOffset + registerCount);
    }

    WriteRegistersRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    //Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, data.length / 2);
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }

    //Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        short[] sdata = convertToShorts(data);
        for (int i = 0; i < sdata.length; i++)
            processImage.writeHoldingRegister(startOffset + i, sdata[i]);
        return new WriteRegistersResponse(slaveId, startOffset, sdata.length);
    }

    //Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_REGISTERS;
    }

    //Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteRegistersResponse(slaveId);
    }

    //Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        ModbusUtils.popUnsignedShort(queue); // register count not needed.
        data = new byte[ModbusUtils.popUnsignedByte(queue)];
        queue.pop(data);
    }
}
