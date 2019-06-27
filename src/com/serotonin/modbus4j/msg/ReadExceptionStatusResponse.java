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

import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * <p>ReadExceptionStatusResponse class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ReadExceptionStatusResponse extends ModbusResponse {
    private byte exceptionStatus;

    ReadExceptionStatusResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    ReadExceptionStatusResponse(int slaveId, byte exceptionStatus) throws ModbusTransportException {
        super(slaveId);
        this.exceptionStatus = exceptionStatus;
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_EXCEPTION_STATUS;
    }

    /** {@inheritDoc} */
    @Override
    protected void readResponse(ByteQueue queue) {
        exceptionStatus = queue.pop();
    }

    /** {@inheritDoc} */
    @Override
    protected void writeResponse(ByteQueue queue) {
        queue.push(exceptionStatus);
    }

    /**
     * <p>Getter for the field <code>exceptionStatus</code>.</p>
     *
     * @return a byte.
     */
    public byte getExceptionStatus() {
        return exceptionStatus;
    }
}
