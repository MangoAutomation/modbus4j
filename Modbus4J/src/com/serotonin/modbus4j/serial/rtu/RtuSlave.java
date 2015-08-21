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
package com.serotonin.modbus4j.serial.rtu;

import java.io.IOException;

import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import com.serotonin.modbus4j.serial.SerialSlave;
import com.serotonin.modbus4j.sero.messaging.MessageControl;

public class RtuSlave extends SerialSlave {
    // Runtime fields
    private MessageControl conn;

    public RtuSlave(SerialPortWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() throws ModbusInitException {
        super.start();

        RtuMessageParser rtuMessageParser = new RtuMessageParser(false);
        RtuRequestHandler rtuRequestHandler = new RtuRequestHandler(this);

        conn = new MessageControl();
        conn.setExceptionHandler(getExceptionHandler());

        try {
            conn.start(transport, rtuMessageParser, rtuRequestHandler, null);
            transport.start("Modbus RTU slave");
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
    }

    @Override
    public void stop() {
        conn.close();
        super.stop();
    }
}
