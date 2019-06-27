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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.base.RangeAndOffset;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.modbus4j.exception.ModbusIdException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.locator.NumericLocator;
import com.serotonin.modbus4j.locator.StringLocator;

/**
 * <p>BasicProcessImage class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class BasicProcessImage implements ProcessImage {
    private final int slaveId;
    private boolean allowInvalidAddress = false;
    private short invalidAddressValue = 0;

    private final Map<Integer, Boolean> coils = new HashMap<>();
    private final Map<Integer, Boolean> inputs = new HashMap<>();
    private final Map<Integer, Short> holdingRegisters = new HashMap<>();
    private final Map<Integer, Short> inputRegisters = new HashMap<>();
    private final List<ProcessImageListener> writeListeners = new ArrayList<>();
    private byte exceptionStatus;

    /**
     * <p>Constructor for BasicProcessImage.</p>
     *
     * @param slaveId a int.
     */
    public BasicProcessImage(int slaveId) {
        ModbusUtils.validateSlaveId(slaveId, false);
        this.slaveId = slaveId;
    }

    /** {@inheritDoc} */
    @Override
    public int getSlaveId() {
        return slaveId;
    }

    /**
     * <p>addListener.</p>
     *
     * @param l a {@link com.serotonin.modbus4j.ProcessImageListener} object.
     */
    public synchronized void addListener(ProcessImageListener l) {
        writeListeners.add(l);
    }

    /**
     * <p>removeListener.</p>
     *
     * @param l a {@link com.serotonin.modbus4j.ProcessImageListener} object.
     */
    public synchronized void removeListener(ProcessImageListener l) {
        writeListeners.remove(l);
    }

    /**
     * <p>isAllowInvalidAddress.</p>
     *
     * @return a boolean.
     */
    public boolean isAllowInvalidAddress() {
        return allowInvalidAddress;
    }

    /**
     * <p>Setter for the field <code>allowInvalidAddress</code>.</p>
     *
     * @param allowInvalidAddress a boolean.
     */
    public void setAllowInvalidAddress(boolean allowInvalidAddress) {
        this.allowInvalidAddress = allowInvalidAddress;
    }

    /**
     * <p>Getter for the field <code>invalidAddressValue</code>.</p>
     *
     * @return a short.
     */
    public short getInvalidAddressValue() {
        return invalidAddressValue;
    }

    /**
     * <p>Setter for the field <code>invalidAddressValue</code>.</p>
     *
     * @param invalidAddressValue a short.
     */
    public void setInvalidAddressValue(short invalidAddressValue) {
        this.invalidAddressValue = invalidAddressValue;
    }

    //
    // /
    // / Additional convenience methods.
    // /
    //
    /**
     * <p>Setter for the field <code>exceptionStatus</code>.</p>
     *
     * @param exceptionStatus a byte.
     */
    public void setExceptionStatus(byte exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    //
    // Binaries
    /**
     * <p>setBinary.</p>
     *
     * @param registerId a int.
     * @param value a boolean.
     */
    public void setBinary(int registerId, boolean value) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        setBinary(rao.getRange(), rao.getOffset(), value);
    }

    /**
     * <p>setBinary.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param value a boolean.
     */
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
    /**
     * <p>setNumeric.</p>
     *
     * @param registerId a int.
     * @param dataType a int.
     * @param value a {@link java.lang.Number} object.
     */
    public synchronized void setNumeric(int registerId, int dataType, Number value) {
        RangeAndOffset rao = new RangeAndOffset(registerId);
        setNumeric(rao.getRange(), rao.getOffset(), dataType, value);
    }

    /**
     * <p>setNumeric.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @param value a {@link java.lang.Number} object.
     */
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

    //
    // Strings
    /**
     * <p>setString.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @param registerCount a int.
     * @param s a {@link java.lang.String} object.
     */
    public synchronized void setString(int range, int offset, int dataType, int registerCount, String s) {
        setString(range, offset, dataType, registerCount, StringLocator.ASCII, s);
    }

    /**
     * <p>setString.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @param registerCount a int.
     * @param charset a {@link java.nio.charset.Charset} object.
     * @param s a {@link java.lang.String} object.
     */
    public synchronized void setString(int range, int offset, int dataType, int registerCount, Charset charset, String s) {
        short[] registers = new StringLocator(slaveId, range, offset, dataType, registerCount, charset)
                .valueToShorts(s);

        // Write the value.
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegister(offset, registers);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegister(offset, registers);
        else
            throw new ModbusIdException("Invalid range to set register: " + range);
    }

    /**
     * <p>setHoldingRegister.</p>
     *
     * @param offset a int.
     * @param registers an array of {@link short} objects.
     */
    public synchronized void setHoldingRegister(int offset, short[] registers) {
        validateOffset(offset);
        for (int i = 0; i < registers.length; i++)
            setHoldingRegister(offset + i, registers[i]);
    }

    /**
     * <p>setInputRegister.</p>
     *
     * @param offset a int.
     * @param registers an array of {@link short} objects.
     */
    public synchronized void setInputRegister(int offset, short[] registers) {
        validateOffset(offset);
        for (int i = 0; i < registers.length; i++)
            setInputRegister(offset + i, registers[i]);
    }

    //
    // Bits
    /**
     * <p>setBit.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param bit a int.
     * @param value a boolean.
     */
    public synchronized void setBit(int range, int offset, int bit, boolean value) {
        if (range == RegisterRange.HOLDING_REGISTER)
            setHoldingRegisterBit(offset, bit, value);
        else if (range == RegisterRange.INPUT_REGISTER)
            setInputRegisterBit(offset, bit, value);
        else
            throw new ModbusIdException("Invalid range to set register: " + range);
    }

    /**
     * <p>setHoldingRegisterBit.</p>
     *
     * @param offset a int.
     * @param bit a int.
     * @param value a boolean.
     */
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

    /**
     * <p>setInputRegisterBit.</p>
     *
     * @param offset a int.
     * @param bit a int.
     * @param value a boolean.
     */
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

    /**
     * <p>getBit.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param bit a int.
     * @return a boolean.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public boolean getBit(int range, int offset, int bit) throws IllegalDataAddressException {
        if (range == RegisterRange.HOLDING_REGISTER)
            return getHoldingRegisterBit(offset, bit);
        if (range == RegisterRange.INPUT_REGISTER)
            return getInputRegisterBit(offset, bit);
        throw new ModbusIdException("Invalid range to get register: " + range);
    }

    /**
     * <p>getHoldingRegisterBit.</p>
     *
     * @param offset a int.
     * @param bit a int.
     * @return a boolean.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public boolean getHoldingRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getHoldingRegister(offset), bit);
    }

    /**
     * <p>getInputRegisterBit.</p>
     *
     * @param offset a int.
     * @param bit a int.
     * @return a boolean.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public boolean getInputRegisterBit(int offset, int bit) throws IllegalDataAddressException {
        validateBit(bit);
        return getBit(getInputRegister(offset), bit);
    }

    /**
     * <p>getNumeric.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @return a {@link java.lang.Number} object.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public Number getNumeric(int range, int offset, int dataType) throws IllegalDataAddressException {
        return getRegister(new NumericLocator(slaveId, range, offset, dataType));
    }

    /**
     * <p>getString.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @param registerCount a int.
     * @return a {@link java.lang.String} object.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public String getString(int range, int offset, int dataType, int registerCount) throws IllegalDataAddressException {
        return getRegister(new StringLocator(slaveId, range, offset, dataType, registerCount, null));
    }

    /**
     * <p>getString.</p>
     *
     * @param range a int.
     * @param offset a int.
     * @param dataType a int.
     * @param registerCount a int.
     * @param charset a {@link java.nio.charset.Charset} object.
     * @return a {@link java.lang.String} object.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public String getString(int range, int offset, int dataType, int registerCount, Charset charset)
            throws IllegalDataAddressException {
        return getRegister(new StringLocator(slaveId, range, offset, dataType, registerCount, charset));
    }

    /**
     * <p>getRegister.</p>
     *
     * @param locator a {@link com.serotonin.modbus4j.locator.BaseLocator} object.
     * @param <T> a T object.
     * @return a T object.
     * @throws com.serotonin.modbus4j.exception.IllegalDataAddressException if any.
     */
    public synchronized <T> T getRegister(BaseLocator<T> locator) throws IllegalDataAddressException {
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
    /** {@inheritDoc} */
    @Override
    public synchronized boolean getCoil(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, coils);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setCoil(int offset, boolean value) {
        validateOffset(offset);
        coils.put(offset, value);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void writeCoil(int offset, boolean value) throws IllegalDataAddressException {
        boolean old = getBoolean(offset, coils);
        setCoil(offset, value);

        for (ProcessImageListener l : writeListeners)
            l.coilWrite(offset, old, value);
    }

    //
    // Inputs
    /** {@inheritDoc} */
    @Override
    public synchronized boolean getInput(int offset) throws IllegalDataAddressException {
        return getBoolean(offset, inputs);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setInput(int offset, boolean value) {
        validateOffset(offset);
        inputs.put(offset, value);
    }

    //
    // Holding registers
    /** {@inheritDoc} */
    @Override
    public synchronized short getHoldingRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, holdingRegisters);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setHoldingRegister(int offset, short value) {
        validateOffset(offset);
        holdingRegisters.put(offset, value);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void writeHoldingRegister(int offset, short value) throws IllegalDataAddressException {
        short old = getShort(offset, holdingRegisters);
        setHoldingRegister(offset, value);

        for (ProcessImageListener l : writeListeners)
            l.holdingRegisterWrite(offset, old, value);
    }

    //
    // Input registers
    /** {@inheritDoc} */
    @Override
    public synchronized short getInputRegister(int offset) throws IllegalDataAddressException {
        return getShort(offset, inputRegisters);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setInputRegister(int offset, short value) {
        validateOffset(offset);
        inputRegisters.put(offset, value);
    }

    //
    // Exception status
    /** {@inheritDoc} */
    @Override
    public byte getExceptionStatus() {
        return exceptionStatus;
    }

    //
    // Report slave id
    /** {@inheritDoc} */
    @Override
    public byte[] getReportSlaveIdData() {
        return new byte[0];
    }

    //
    //
    // Private
    //
    private short getShort(int offset, Map<Integer, Short> map) throws IllegalDataAddressException {
        Short value = map.get(offset);
        if (value == null) {
            if (allowInvalidAddress)
                return invalidAddressValue;
            throw new IllegalDataAddressException();
        }
        return value.shortValue();
    }

    private boolean getBoolean(int offset, Map<Integer, Boolean> map) throws IllegalDataAddressException {
        Boolean value = map.get(offset);
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
