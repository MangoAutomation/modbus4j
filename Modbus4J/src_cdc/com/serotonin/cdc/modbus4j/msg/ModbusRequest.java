/*
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
import com.serotonin.cdc.modbus4j.code.ExceptionCode;
import com.serotonin.cdc.modbus4j.code.FunctionCode;
import com.serotonin.cdc.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.util.queue.ByteQueue;

abstract public class ModbusRequest extends ModbusMessage {
    public static ModbusRequest createModbusRequest(ByteQueue queue) throws ModbusTransportException {
        int slaveId = ModbusUtils.popUnsignedByte(queue);
        byte functionCode = queue.pop();

        ModbusRequest request = null;
        if (functionCode == FunctionCode.READ_COILS)
            request = new ReadCoilsRequest(slaveId);
        else if (functionCode == FunctionCode.READ_DISCRETE_INPUTS)
            request = new ReadDiscreteInputsRequest(slaveId);
        else if (functionCode == FunctionCode.READ_HOLDING_REGISTERS)
            request = new ReadHoldingRegistersRequest(slaveId);
        else if (functionCode == FunctionCode.READ_INPUT_REGISTERS)
            request = new ReadInputRegistersRequest(slaveId);
        else if (functionCode == FunctionCode.WRITE_COIL)
            request = new WriteCoilRequest(slaveId);
        else if (functionCode == FunctionCode.WRITE_REGISTER)
            request = new WriteRegisterRequest(slaveId);
        else if (functionCode == FunctionCode.READ_EXCEPTION_STATUS)
            request = new ReadExceptionStatusRequest(slaveId);
        else if (functionCode == FunctionCode.WRITE_COILS)
            request = new WriteCoilsRequest(slaveId);
        else if (functionCode == FunctionCode.WRITE_REGISTERS)
            request = new WriteRegistersRequest(slaveId);
        else if (functionCode == FunctionCode.REPORT_SLAVE_ID)
            request = new ReportSlaveIdRequest(slaveId);
        // else if (functionCode == FunctionCode.WRITE_MASK_REGISTER)
        // request = new WriteMaskRegisterRequest(slaveId);
        else
            request = new ExceptionRequest(slaveId, functionCode, ExceptionCode.ILLEGAL_FUNCTION);

        request.readRequest(queue);

        return request;
    }

    ModbusRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    abstract public void validate(Modbus modbus) throws ModbusTransportException;

    public ModbusResponse handle(ProcessImage processImage) throws ModbusTransportException {
        try {
            try {
                return handleImpl(processImage);
            }
            catch (IllegalDataAddressException e) {
                return handleException(ExceptionCode.ILLEGAL_DATA_ADDRESS);
            }
        }
        catch (Exception e) {
            return handleException(ExceptionCode.SLAVE_DEVICE_FAILURE);
        }
    }

    abstract ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException;

    abstract protected void readRequest(ByteQueue queue);

    ModbusResponse handleException(byte exceptionCode) throws ModbusTransportException {
        ModbusResponse response = getResponseInstance(slaveId);
        response.setException(exceptionCode);
        return response;
    }

    abstract ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException;

    //Override
    final protected void writeImpl(ByteQueue queue) {
        queue.push(getFunctionCode());
        writeRequest(queue);
    }

    abstract protected void writeRequest(ByteQueue queue);
}
