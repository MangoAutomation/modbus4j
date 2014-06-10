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
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.util.queue.ByteQueue;

abstract public class ReadBinaryRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfBits;

    public ReadBinaryRequest(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        this.numberOfBits = numberOfBits;
    }

    //Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        ModbusUtils.validateOffset(startOffset);
        modbus.validateNumberOfBits(numberOfBits);
        ModbusUtils.validateEndOffset(startOffset + numberOfBits);
    }

    ReadBinaryRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    //Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfBits);
    }

    //Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfBits = ModbusUtils.popUnsignedShort(queue);
    }

    protected byte[] getData(ProcessImage processImage) throws ModbusTransportException {
        boolean[] data = new boolean[numberOfBits];

        // Get the data from the process image.
        for (int i = 0; i < numberOfBits; i++)
            data[i] = getBinary(processImage, i + startOffset);

        // Convert the boolean array into an array of bytes.
        return convertToBytes(data);
    }

    abstract protected boolean getBinary(ProcessImage processImage, int index) throws ModbusTransportException;
}
