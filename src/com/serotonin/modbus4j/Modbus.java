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
package com.serotonin.modbus4j;

import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.sero.messaging.DefaultMessagingExceptionHandler;
import com.serotonin.modbus4j.sero.messaging.MessagingExceptionHandler;

/**
 * Base level for masters and slaves/listeners
 * 
 * TODO: - handle echoing in RS485
 * 
 * @author mlohbihler
 */
public class Modbus {
    public static final int DEFAULT_MAX_READ_BIT_COUNT = 2000;
    public static final int DEFAULT_MAX_READ_REGISTER_COUNT = 125;
    public static final int DEFAULT_MAX_WRITE_REGISTER_COUNT = 120;

    private MessagingExceptionHandler exceptionHandler = new DefaultMessagingExceptionHandler();

    private int maxReadBitCount = DEFAULT_MAX_READ_BIT_COUNT;
    private int maxReadRegisterCount = DEFAULT_MAX_READ_REGISTER_COUNT;
    private int maxWriteRegisterCount = DEFAULT_MAX_WRITE_REGISTER_COUNT;

    public int getMaxReadCount(int registerRange) {
        switch (registerRange) {
        case RegisterRange.COIL_STATUS:
        case RegisterRange.INPUT_STATUS:
            return maxReadBitCount;
        case RegisterRange.HOLDING_REGISTER:
        case RegisterRange.INPUT_REGISTER:
            return maxReadRegisterCount;
        }
        return -1;
    }

    public void validateNumberOfBits(int bits) throws ModbusTransportException {
        if (bits < 1 || bits > maxReadBitCount)
            throw new ModbusTransportException("Invalid number of bits: " + bits);
    }

    public void validateNumberOfRegisters(int registers) throws ModbusTransportException {
        if (registers < 1 || registers > maxReadRegisterCount)
            throw new ModbusTransportException("Invalid number of registers: " + registers);
    }

    public void setExceptionHandler(MessagingExceptionHandler exceptionHandler) {
        if (exceptionHandler == null)
            this.exceptionHandler = new DefaultMessagingExceptionHandler();
        else
            this.exceptionHandler = exceptionHandler;
    }

    public MessagingExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public int getMaxReadBitCount() {
        return maxReadBitCount;
    }

    public void setMaxReadBitCount(int maxReadBitCount) {
        this.maxReadBitCount = maxReadBitCount;
    }

    public int getMaxReadRegisterCount() {
        return maxReadRegisterCount;
    }

    public void setMaxReadRegisterCount(int maxReadRegisterCount) {
        this.maxReadRegisterCount = maxReadRegisterCount;
    }

    public int getMaxWriteRegisterCount() {
        return maxWriteRegisterCount;
    }

    public void setMaxWriteRegisterCount(int maxWriteRegisterCount) {
        this.maxWriteRegisterCount = maxWriteRegisterCount;
    }
}
