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

import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.code.ExceptionCode;
import com.serotonin.cdc.modbus4j.code.FunctionCode;
import com.serotonin.cdc.modbus4j.exception.IllegalFunctionException;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.util.queue.ByteQueue;

abstract public class ModbusResponse extends ModbusMessage {
    protected static final byte MAX_FUNCTION_CODE = (byte) 0x80;

    public static ModbusResponse createModbusResponse(ByteQueue queue) throws ModbusTransportException {
        int slaveId = ModbusUtils.popUnsignedByte(queue);
        byte functionCode = queue.pop();
        boolean isException = false;

        if (greaterThan(functionCode, MAX_FUNCTION_CODE)) {
            isException = true;
            functionCode -= MAX_FUNCTION_CODE;
        }

        ModbusResponse response = null;
        if (functionCode == FunctionCode.READ_COILS)
            response = new ReadCoilsResponse(slaveId);
        else if (functionCode == FunctionCode.READ_DISCRETE_INPUTS)
            response = new ReadDiscreteInputsResponse(slaveId);
        else if (functionCode == FunctionCode.READ_HOLDING_REGISTERS)
            response = new ReadHoldingRegistersResponse(slaveId);
        else if (functionCode == FunctionCode.READ_INPUT_REGISTERS)
            response = new ReadInputRegistersResponse(slaveId);
        else if (functionCode == FunctionCode.WRITE_COIL)
            response = new WriteCoilResponse(slaveId);
        else if (functionCode == FunctionCode.WRITE_REGISTER)
            response = new WriteRegisterResponse(slaveId);
        else if (functionCode == FunctionCode.READ_EXCEPTION_STATUS)
            response = new ReadExceptionStatusResponse(slaveId);
        else if (functionCode == FunctionCode.WRITE_COILS)
            response = new WriteCoilsResponse(slaveId);
        else if (functionCode == FunctionCode.WRITE_REGISTERS)
            response = new WriteRegistersResponse(slaveId);
        else if (functionCode == FunctionCode.REPORT_SLAVE_ID)
            response = new ReportSlaveIdResponse(slaveId);
        else if (functionCode == FunctionCode.WRITE_MASK_REGISTER)
            response = new WriteMaskRegisterResponse(slaveId);
        else
            throw new IllegalFunctionException(functionCode, slaveId);

        response.read(queue, isException);

        return response;
    }

    protected byte exceptionCode = -1;

    ModbusResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    public boolean isException() {
        return exceptionCode != -1;
    }

    public String getExceptionMessage() {
        return ExceptionCode.getExceptionMessage(exceptionCode);
    }

    void setException(byte exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public byte getExceptionCode() {
        return exceptionCode;
    }

    //Override
    final protected void writeImpl(ByteQueue queue) {
        if (isException()) {
            queue.push((byte) (getFunctionCode() + MAX_FUNCTION_CODE));
            queue.push(exceptionCode);
        }
        else {
            queue.push(getFunctionCode());
            writeResponse(queue);
        }
    }

    abstract protected void writeResponse(ByteQueue queue);

    void read(ByteQueue queue, boolean isException) {
        if (isException)
            exceptionCode = queue.pop();
        else
            readResponse(queue);
    }

    abstract protected void readResponse(ByteQueue queue);

    private static boolean greaterThan(byte b1, byte b2) {
        int i1 = b1 & 0xff;
        int i2 = b2 & 0xff;
        return i1 > i2;
    }

    public static void main(String[] args) throws Exception {
        ByteQueue queue = new ByteQueue(new byte[] { 3, 2 });
        ModbusResponse r = createModbusResponse(queue);
        System.out.println(r);
    }
}
