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

import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.util.queue.ByteQueue;

abstract public class ModbusMessage {
    protected int slaveId;

    public ModbusMessage(int slaveId) throws ModbusTransportException {
        // Validate the node id. Note that a 0 slave id is a broadcast message.
        if (slaveId < 0 || slaveId > 247)
            throw new ModbusTransportException("Invalid slave id", slaveId);

        this.slaveId = slaveId;
    }

    public int getSlaveId() {
        return slaveId;
    }

    abstract public byte getFunctionCode();

    final public void write(ByteQueue queue) {
        ModbusUtils.pushByte(queue, slaveId);
        writeImpl(queue);
    }

    abstract protected void writeImpl(ByteQueue queue);

    protected byte[] convertToBytes(boolean[] bdata) {
        int byteCount = (bdata.length + 7) / 8;
        byte[] data = new byte[byteCount];
        for (int i = 0; i < bdata.length; i++)
            data[i / 8] |= (bdata[i] ? 1 : 0) << (i % 8);
        return data;
    }

    protected byte[] convertToBytes(short[] sdata) {
        int byteCount = sdata.length * 2;
        byte[] data = new byte[byteCount];
        for (int i = 0; i < sdata.length; i++) {
            data[i * 2] = (byte) (0xff & (sdata[i] >> 8));
            data[i * 2 + 1] = (byte) (0xff & sdata[i]);
        }
        return data;
    }

    protected boolean[] convertToBooleans(byte[] data) {
        boolean[] bdata = new boolean[data.length * 8];
        for (int i = 0; i < bdata.length; i++)
            bdata[i] = ((data[i / 8] >> (i % 8)) & 0x1) == 1;
        return bdata;
    }

    protected short[] convertToShorts(byte[] data) {
        short[] sdata = new short[data.length / 2];
        for (int i = 0; i < sdata.length; i++)
            sdata[i] = ModbusUtils.toShort(data[i * 2], data[i * 2 + 1]);
        return sdata;
    }
}
