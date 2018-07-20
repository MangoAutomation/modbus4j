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

import com.serotonin.modbus4j.exception.IllegalDataAddressException;

/**
 * Used by slave implementors. Provides an interface by which slaves can easily manage data.
 * 
 * @author mlohbihler
 */
public interface ProcessImage {
    int getSlaveId();

    //
    // /
    // / Coils
    // /
    //
    /**
     * Returns the current value of the coil for the given offset.
     * 
     * @param offset
     * @return the value of the coil
     */
    boolean getCoil(int offset) throws IllegalDataAddressException;

    /**
     * Used internally for setting the value of the coil.
     * 
     * @param offset
     * @param value
     */
    void setCoil(int offset, boolean value);

    /**
     * Used to set the coil as a result of a write command from the master.
     * 
     * @param offset
     * @param value
     */
    void writeCoil(int offset, boolean value) throws IllegalDataAddressException;

    //
    // /
    // / Inputs
    // /
    //
    /**
     * Returns the current value of the input for the given offset.
     * 
     * @param offset
     * @return the value of the input
     */
    boolean getInput(int offset) throws IllegalDataAddressException;

    /**
     * Used internally for setting the value of the input.
     * 
     * @param offset
     * @param value
     */
    void setInput(int offset, boolean value);

    //
    // /
    // / Holding registers
    // /
    //
    /**
     * Returns the current value of the holding register for the given offset.
     * 
     * @param offset
     * @return the value of the register
     */
    short getHoldingRegister(int offset) throws IllegalDataAddressException;

    /**
     * Used internally for setting the value of the holding register.
     * 
     * @param offset
     * @param value
     */
    void setHoldingRegister(int offset, short value);

    /**
     * Used to set the holding register as a result of a write command from the master.
     * 
     * @param offset
     * @param value
     */
    void writeHoldingRegister(int offset, short value) throws IllegalDataAddressException;

    //
    // /
    // / Input registers
    // /
    //
    /**
     * Returns the current value of the input register for the given offset.
     * 
     * @param offset
     * @return the value of the register
     */
    short getInputRegister(int offset) throws IllegalDataAddressException;

    /**
     * Used internally for setting the value of the input register.
     * 
     * @param offset
     * @param value
     */
    void setInputRegister(int offset, short value);

    //
    // /
    // / Exception status
    // /
    //
    /**
     * Returns the current value of the exception status.
     * 
     * @return the current value of the exception status.
     */
    byte getExceptionStatus();

    //
    // /
    // / Report slave id
    // /
    //
    /**
     * Returns the data for the report slave id command.
     * 
     * @return the data for the report slave id command.
     */
    byte[] getReportSlaveIdData();
}
