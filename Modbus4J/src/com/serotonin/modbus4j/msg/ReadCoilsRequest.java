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

import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;

public class ReadCoilsRequest extends ReadBinaryRequest {
    public ReadCoilsRequest(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        super(slaveId, startOffset, numberOfBits);
    }

    ReadCoilsRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_COILS;
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        return new ReadCoilsResponse(slaveId, getData(processImage));
    }

    @Override
    protected boolean getBinary(ProcessImage processImage, int index) throws ModbusTransportException {
        return processImage.getCoil(index);
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new ReadCoilsResponse(slaveId);
    }

    @Override
    public String toString() {
        return "ReadCoilsRequest [slaveId=" + slaveId + ", getFunctionCode()=" + getFunctionCode() + ", toString()="
                + super.toString() + "]";
    }
}
