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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.serotonin.modbus4j.exception.ModbusInitException;

abstract public class ModbusSlaveSet extends Modbus {
	
    private LinkedHashMap<Integer, ProcessImage> processImages = new LinkedHashMap<>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addProcessImage(ProcessImage processImage) {
    	lock.writeLock().lock();
    	try{
    		processImages.put(processImage.getSlaveId(), processImage);
    	}finally{
    		lock.writeLock().unlock();
    	}
    }
    
    public boolean removeProcessImage(int slaveId){
    	lock.writeLock().lock();
    	try{
    		return (processImages.remove(slaveId) != null);
    	}finally{
    		lock.writeLock().unlock();
    	}
    }

    public boolean removeProcessImage(ProcessImage processImage){
    	lock.writeLock().lock();
    	try{
    		return (processImages.remove(processImage.getSlaveId()) != null);
    	}finally{
    		lock.writeLock().unlock();
    	}
    }


    public ProcessImage getProcessImage(int slaveId) {
    	lock.readLock().lock();
    	try{
    		return processImages.get(slaveId);
    	}finally{
    		lock.readLock().unlock();
    	}
    }

    /**
     * Get a copy of the current process images
     * @return
     */
    public Collection<ProcessImage> getProcessImages() {
    	lock.readLock().lock();
    	try{
    		return new HashSet<>(processImages.values());
    	}finally{
    		lock.readLock().unlock();
    	}
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
