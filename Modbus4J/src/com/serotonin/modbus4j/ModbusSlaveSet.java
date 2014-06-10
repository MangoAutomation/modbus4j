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

import java.util.Collection;
import java.util.LinkedHashMap;

import com.serotonin.modbus4j.exception.ModbusInitException;

abstract public class ModbusSlaveSet extends Modbus {
    protected LinkedHashMap<Integer, ProcessImage> processImages = new LinkedHashMap<Integer, ProcessImage>();

    public void addProcessImage(ProcessImage processImage) {
        processImages.put(processImage.getSlaveId(), processImage);
    }

    public ProcessImage getProcessImage(int slaveId) {
        return processImages.get(slaveId);
    }

    public Collection<ProcessImage> getProcessImages() {
        return processImages.values();
    }

    /**
     * Starts the slave. If an exception is not thrown, this method does not return, but uses the thread to execute the
     * listening.
     * 
     * @throws ModbusInitException
     */
    abstract public void start() throws ModbusInitException;

    abstract public void stop();
}
