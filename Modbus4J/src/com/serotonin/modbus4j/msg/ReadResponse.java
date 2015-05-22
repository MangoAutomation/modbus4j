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
package com.serotonin.modbus4j.msg;

import com.serotonin.io.StreamUtils;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.util.queue.ByteQueue;

abstract public class ReadResponse extends ModbusResponse {
    private byte[] data;

    ReadResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    ReadResponse(int slaveId, byte[] data) throws ModbusTransportException {
        super(slaveId);
        this.data = data;
    }

    @Override
    protected void readResponse(ByteQueue queue) {
        int numberOfBytes = ModbusUtils.popUnsignedByte(queue);
        if (queue.size() < numberOfBytes)
            throw new ArrayIndexOutOfBoundsException();

        data = new byte[numberOfBytes];
        queue.pop(data);
    }

    @Override
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }
    
    /**
     * check the Response's numberBytesDeclare if equal  request's numberBytesDeclare
     */
    @Override
    public void  validateResponse(ModbusRequest request) throws ModbusTransportException {
    	super.validateResponse(request);
        if(numberBytesDeclare!=request.getNumberBytesDeclare()*2)
        	throw new ModbusTransportException("Response NumberByte not expect request", request.getSlaveId());         	
    }

    public byte[] getData() {
        return data;
    }

    public short[] getShortData() {
        return convertToShorts(data);
    }

    public boolean[] getBooleanData() {
        return convertToBooleans(data);
    }

    public String toString(boolean numeric) {
        if (data == null)
            return "ReadResponse [null]";
        return "ReadResponse [len=" + (numeric ? data.length / 2 : data.length * 8) + ", " + StreamUtils.dumpHex(data)
                + "]";
    }
}
