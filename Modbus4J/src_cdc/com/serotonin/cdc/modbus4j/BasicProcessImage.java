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
package com.serotonin.cdc.modbus4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.base.RangeAndOffset;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.cdc.modbus4j.exception.ModbusIdException;
import com.serotonin.cdc.modbus4j.locator.BaseLocator;
import com.serotonin.cdc.modbus4j.locator.NumericLocator;
import com.serotonin.cdc.modbus4j.locator.StringLocator;

public class BasicProcessImage implements ProcessImage {
    private final int slaveId;
    private boolean allowInvalidAddress = false;
    private short invalidAddressValue = 0;

    private final Map coils = new HashMap();
    private final Map inputs = new HashMap();
    private final Map holdingRegisters = new HashMap();
    private final Map inputRegisters = new HashMap();
    private final List writeListeners = new ArrayList();
    private byte exceptionStatus;

    public BasicProcessImage(int slaveId) {
        ModbusUtils.validateSlaveId(slaveId, false);
        this.slaveId = slaveId;
    }

    //Override
    public int getSlaveId() {
        return slaveId;
    }

    public synchronized void addListener(ProcessImageListener l) {
        writeListeners.add(l);
    }

    public synchronized void removeListener(ProcessImageListener l) {
        writeListeners.remove(l);
    }

    public boolean isAllowInvalidAddress() {
        return allowInvalidAddress;
    }

    public void setAllowInvalidAddress(boolean allowInvalidAddress) {
        this.allowInvalidAddress = allowInvalidAddress;
    }

    public short getInvalidAddressValue() {
        return invalidAddressValue;
    }

    public void setInvalidAddressValue(short invalidAddressValue) {
        this.invalidAddressValue = invalidAddressValue;
    }

    //
    // /
    // / Additional convenience methods.
    // /
    //
    public void setExceptionStatus(byte exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    //
    // Binaries
    public void setBinary(int registerId, boolean value) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        setBinary(rao.getRange(), rao.getOffset(), value);
    }

    public void setBinary(int range, int offset, boolean value) {
        if (range == RegisterRange.COIL_STATUS)
            setCoil(offset, value);
        else if (range == RegisterRange.INPUT_STATUS)
            setInput(offset, value);
        else
            throw new ModbusIdException("Invalid range to set binary: " + range);
    }

    //
    // Numerics
    public synchronized void setNumeric(int registerId, int dataType, Number value) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        setNumeric(rao.getRange(), rao.getOffset(), dataType, value);
    }

    public synchronized void setNumeric(int range, int offset, int dataType, Number value) {
        short[] registers = new NumericLocator(slaveId, range, offset, dataType).valueToShorts(value);

        // Write the value.
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegister(offset, registers);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegister(offset, registers);
        else
            throw new ModbusIdException("Invalid range to set register: " + range);
    }

    public synchronized void setString(int range, int offset, int dataType, int registerCount, String s) {
        short[] registers = new StringLocator(slaveId, range, offset, dataType, registerCount).valueToShorts(s);

        // Write the value.
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegister(offset, registers);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegister(offset, registers);
        else
            throw new ModbusIdException("Invalid range to set register: " + range);
    }

    public synchronized void setHoldingRegister(int offset, short[] registers) {
        validateOffset(offset);
        for (int i = 0; i < registers.length; i++)
            setHoldingRegister(offset + i, registers[i]);
    }

    public synchronized void setInputRegister(int offset, short[] registers) {
        validateOffset(offset);
        for (int i = 0; i < registers.length; i++)
            setInputRegister(offset + i, registers[i]);
    }

    //
    // Bits
    public synchronized void setBit(int range, int offset, int bit, boolean value) {
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegisterBit(offset, bit, value);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegisterBit(offset, bit, value);
        else
            throw new ModbusIdException("Invalid range to set register: " + range);
    }

    public synchronized void setHoldingRegisterBit(int offset, int bit, boolean value) {
        validateBit(bit);
        short s;
        try {
            s = getHoldingRegister(offset);
        }
        catch (IllegalDataAddressException e) {
            s = 0;
        }
        setHoldingRegister(offset, setBit(s, bit, value));
    }

    public synchronized void setInputRegisterBit(int offset, int bit, boolean value) {
        validateBit(bit);
        short s;
        try {
            s = getInputRegister(offset);
        }
        catch (IllegalDataAddressException e) {
            s = 0;
        }
        setInputRegister(offset, setBit(s, bit, value));
    }

    public boolean getBit(int range, int offset, int bit) throws IllegalDataAddressException {
        if (range == RegisterRange.HOLDING_REGISTER)
            return getHoldingRegisterBit(offset, bit);
        if (range == RegisterRange.INPUT_REGISTER)
            return getInputRegisterBit(offset, bit);
        throw new ModbusIdException("Invalid range to get register: " + range);
    }

    public boolean getHoldingRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getHoldingRegister(offset), bit);
    }

    public boolean getInputRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getInputRegister(offset), bit);
    }

    public Number getNumeric(int range, int offset, int dataType) throws IllegalDataAddressException {
        return (Number) getRegister(new NumericLocator(slaveId, range, offset, dataType));
    }

    public String getString(int range, int offset, int dataType, int registerCount) throws IllegalDataAddressException {
        return (String) getRegister(new StringLocator(slaveId, range, offset, dataType, registerCount));
    }

    /***
     * public String getString(int range, int offset, int dataType, int registerCount, Charset charset) throws
     * IllegalDataAddressException { return (String)getRegister(new StringLocator(slaveId, range, offset, dataType,
     * registerCount, charset)); }
     ***/
    public synchronized Object getRegister(BaseLocator locator) throws IllegalDataAddressException {
        int words = locator.getRegisterCount();
        byte[] b = new byte[locator.getRegisterCount() * 2];
        for (int i = 0; i < words; i++) {
            short s;
            if (locator.getRange() == RegisterRange.INPUT_REGISTER)
                s = getInputRegister(locator.getOffset() + i);
            else if (locator.getRange() == RegisterRange.HOLDING_REGISTER)
                s = getHoldingRegister(locator.getOffset() + i);
            else if (allowInvalidAddress)
                s = invalidAddressValue;
            else
                throw new IllegalDataAddressException();
            b[i * 2] = ModbusUtils.toByte(s, true);
            b[i * 2 + 1] = ModbusUtils.toByte(s, false);
        }

        return locator.bytesToValueRealOffset(b, 0);
    }

    //
    //
    // ProcessImage interface
    //

    //
    // Coils
    public synchronized boolean getCoil(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, coils);
    }

    public synchronized void setCoil(int offset, boolean value) {
        validateOffset(offset);
        coils.put(new Integer(offset), new Boolean(value));
    }

    public synchronized void writeCoil(int offset, boolean value) throws IllegalDataAddressException {
        boolean old = getBoolean(offset, coils);
        setCoil(offset, value);

        for (Iterator iter = writeListeners.iterator(); iter.hasNext();) {
            ProcessImageListener l = (ProcessImageListener) iter.next();
            l.coilWrite(offset, old, value);
        }
    }

    //
    // Inputs
    public synchronized boolean getInput(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, inputs);
    }

    public synchronized void setInput(int offset, boolean value) {
        validateOffset(offset);
        inputs.put(new Integer(offset), new Boolean(value));
    }

    //
    // Holding registers
    public synchronized short getHoldingRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, holdingRegisters);
    }

    public synchronized void setHoldingRegister(int offset, short value) {
        validateOffset(offset);
        holdingRegisters.put(new Integer(offset), new Short(value));
    }

    public synchronized void writeHoldingRegister(int offset, short value) throws IllegalDataAddressException {
        short old = getShort(offset, holdingRegisters);
        setHoldingRegister(offset, value);

        for (Iterator iter = writeListeners.iterator(); iter.hasNext();) {
            ProcessImageListener l = (ProcessImageListener) iter.next();
            l.holdingRegisterWrite(offset, old, value);
        }
    }

    //
    // Input registers
    public synchronized short getInputRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, inputRegisters);
    }

    public synchronized void setInputRegister(int offset, short value) {
        validateOffset(offset);
        inputRegisters.put(new Integer(offset), new Short(value));
    }

    //
    // Exception status
    public byte getExceptionStatus() {
        return exceptionStatus;
    }

    //
    // Report slave id
    public byte[] getReportSlaveIdData() {
        return new byte[0];
    }

    //
    //
    // Private
    //
    private short getShort(int offset, Map map) throws IllegalDataAddressException {
        Short value = (Short) map.get(new Integer(offset));
        if (value == null) {
            if (allowInvalidAddress)
                return invalidAddressValue;
            throw new IllegalDataAddressException();
        }
        return value.shortValue();
    }

    private boolean getBoolean(int offset, Map map) throws IllegalDataAddressException {
        Boolean value = (Boolean) map.get(new Integer(offset));
        if (value == null) {
            if (allowInvalidAddress)
                return false;
            throw new IllegalDataAddressException();
        }
        return value.booleanValue();
    }

    private void validateOffset(int offset) {
        if (offset < 0 || offset > 65535)
            throw new ModbusIdException("Invalid offset: " + offset);
    }

    private void validateBit(int bit) {
        if (bit < 0 || bit > 15)
            throw new ModbusIdException("Invalid bit: " + bit);
    }

    private short setBit(short s, int bit, boolean value) {
        return (short) (s | ((value ? 1 : 0) << bit));
    }

    private boolean getBit(short s, int bit) {
        return ((s >> bit) & 0x1) == 1;
    }
}
