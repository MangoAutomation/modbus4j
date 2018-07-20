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
package com.serotonin.cdc.modbus4j.base;

import java.util.Iterator;

import com.serotonin.cdc.messaging.RequestHandler;
import com.serotonin.cdc.modbus4j.ModbusSlaveSet;
import com.serotonin.cdc.modbus4j.ProcessImage;
import com.serotonin.cdc.modbus4j.exception.ModbusTransportException;
import com.serotonin.cdc.modbus4j.msg.ModbusRequest;
import com.serotonin.cdc.modbus4j.msg.ModbusResponse;

abstract public class BaseRequestHandler implements RequestHandler {
    protected ModbusSlaveSet slave;

    public BaseRequestHandler(ModbusSlaveSet slave) {
        this.slave = slave;
    }

    protected ModbusResponse handleRequestImpl(ModbusRequest request) throws ModbusTransportException {
        request.validate(slave);

        int slaveId = request.getSlaveId();

        // Check the slave id.
        if (slaveId == 0) {
            // Broadcast message. Send to all process images.
            for (Iterator iter = slave.getProcessImages().iterator(); iter.hasNext();) {
                ProcessImage processImage;
                processImage = (ProcessImage) iter.next();
                request.handle(processImage);
            }
            return null;
        }

        // Find the process image to which to send.
        ProcessImage processImage = slave.getProcessImage(slaveId);
        if (processImage == null)
            return null;

        return request.handle(processImage);
    }
}
