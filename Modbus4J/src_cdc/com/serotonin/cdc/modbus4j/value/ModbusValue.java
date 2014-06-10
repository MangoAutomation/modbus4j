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
package com.serotonin.cdc.modbus4j.value;

import java.math.BigInteger;

import com.serotonin.cdc.modbus4j.code.DataType;
import com.serotonin.cdc.modbus4j.exception.InvalidDataConversionException;

abstract public class ModbusValue {
    private final DataType type;
    private final Object value;

    public ModbusValue(DataType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DataType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public boolean booleanValue() {
        if (value instanceof Boolean)
            return ((Boolean) value).booleanValue();
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to boolean");
    }

    public int intValue() {
        if (value instanceof Integer)
            return ((Integer) value).intValue();
        if (value instanceof Short)
            return ((Short) value).shortValue() & 0xffff;
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to int");
    }

    public long longValue() {
        if (value instanceof Long)
            return ((Long) value).longValue();
        if (value instanceof Integer)
            return ((Integer) value).intValue() & 0xffffffff;
        if (value instanceof Short)
            return ((Short) value).shortValue() & 0xffff;
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to long");
    }

    public BigInteger bigIntegerValue() {
        if (value instanceof BigInteger)
            return (BigInteger) value;
        if (value instanceof Long)
            return BigInteger.valueOf(((Long) value).longValue());
        if (value instanceof Integer)
            return BigInteger.valueOf(((Integer) value).intValue() & 0xffffffff);
        if (value instanceof Short)
            return BigInteger.valueOf(((Short) value).shortValue() & 0xffff);
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to BigInteger");
    }

    public float floatValue() {
        if (value instanceof Float)
            return ((Float) value).floatValue();
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to float");
    }

    public double doubleValue() {
        if (value instanceof Double)
            return ((Double) value).doubleValue();
        if (value instanceof Float)
            return ((Float) value).doubleValue();
        throw new InvalidDataConversionException("Can't convert " + value.getClass() + " to float");
    }
}
