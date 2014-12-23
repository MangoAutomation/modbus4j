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
package com.serotonin.modbus4j.code;

import java.math.BigInteger;

/**
 * @author Matthew Lohbihler
 */
public class DataType {
    public static final int BINARY = 1;

    public static final int TWO_BYTE_INT_UNSIGNED = 2;
    public static final int TWO_BYTE_INT_SIGNED = 3;
    public static final int TWO_BYTE_INT_UNSIGNED_SWAPPED = 22;
    public static final int TWO_BYTE_INT_SIGNED_SWAPPED = 23;

    public static final int FOUR_BYTE_INT_UNSIGNED = 4;
    public static final int FOUR_BYTE_INT_SIGNED = 5;
    public static final int FOUR_BYTE_INT_UNSIGNED_SWAPPED = 6;
    public static final int FOUR_BYTE_INT_SIGNED_SWAPPED = 7;
    /* 0xAABBCCDD is transmitted as 0xDDCCBBAA */
    public static final int FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED = 24;
    public static final int FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED = 25;
    
    public static final int FOUR_BYTE_FLOAT = 8;
    public static final int FOUR_BYTE_FLOAT_SWAPPED = 9;
    public static final int FOUR_BYTE_FLOAT_SWAPPED_INVERTED = 21;

    public static final int EIGHT_BYTE_INT_UNSIGNED = 10;
    public static final int EIGHT_BYTE_INT_SIGNED = 11;
    public static final int EIGHT_BYTE_INT_UNSIGNED_SWAPPED = 12;
    public static final int EIGHT_BYTE_INT_SIGNED_SWAPPED = 13;
    public static final int EIGHT_BYTE_FLOAT = 14;
    public static final int EIGHT_BYTE_FLOAT_SWAPPED = 15;

    public static final int TWO_BYTE_BCD = 16;
    public static final int FOUR_BYTE_BCD = 17;
    public static final int FOUR_BYTE_BCD_SWAPPED = 20;

    public static final int CHAR = 18;
    public static final int VARCHAR = 19;

    public static int getRegisterCount(int id) {
        switch (id) {
        case BINARY:
        case TWO_BYTE_INT_UNSIGNED:
        case TWO_BYTE_INT_SIGNED:
        case TWO_BYTE_INT_UNSIGNED_SWAPPED:
        case TWO_BYTE_INT_SIGNED_SWAPPED:
        case TWO_BYTE_BCD:
            return 1;
        case FOUR_BYTE_INT_UNSIGNED:
        case FOUR_BYTE_INT_SIGNED:
        case FOUR_BYTE_INT_UNSIGNED_SWAPPED:
        case FOUR_BYTE_INT_SIGNED_SWAPPED:
        case FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED:
        case FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED:
        case FOUR_BYTE_FLOAT:
        case FOUR_BYTE_FLOAT_SWAPPED:
        case FOUR_BYTE_FLOAT_SWAPPED_INVERTED:
        case FOUR_BYTE_BCD:
        case FOUR_BYTE_BCD_SWAPPED:
            return 2;
        case EIGHT_BYTE_INT_UNSIGNED:
        case EIGHT_BYTE_INT_SIGNED:
        case EIGHT_BYTE_INT_UNSIGNED_SWAPPED:
        case EIGHT_BYTE_INT_SIGNED_SWAPPED:
        case EIGHT_BYTE_FLOAT:
        case EIGHT_BYTE_FLOAT_SWAPPED:
            return 4;
        }
        return 0;
    }

    public static Class<?> getJavaType(int id) {
        switch (id) {
        case BINARY:
            return Boolean.class;
        case TWO_BYTE_INT_UNSIGNED:
        case TWO_BYTE_INT_UNSIGNED_SWAPPED:
            return Integer.class;
        case TWO_BYTE_INT_SIGNED:
        case TWO_BYTE_INT_SIGNED_SWAPPED:
            return Short.class;
        case FOUR_BYTE_INT_UNSIGNED:
            return Long.class;
        case FOUR_BYTE_INT_SIGNED:
            return Integer.class;
        case FOUR_BYTE_INT_UNSIGNED_SWAPPED:
        case FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED:
        	return Long.class;
        case FOUR_BYTE_INT_SIGNED_SWAPPED:
        case FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED:
            return Integer.class;
        case FOUR_BYTE_FLOAT:
            return Float.class;
        case FOUR_BYTE_FLOAT_SWAPPED:
            return Float.class;
        case FOUR_BYTE_FLOAT_SWAPPED_INVERTED:
            return Float.class;
        case EIGHT_BYTE_INT_UNSIGNED:
            return BigInteger.class;
        case EIGHT_BYTE_INT_SIGNED:
            return Long.class;
        case EIGHT_BYTE_INT_UNSIGNED_SWAPPED:
            return BigInteger.class;
        case EIGHT_BYTE_INT_SIGNED_SWAPPED:
            return Long.class;
        case EIGHT_BYTE_FLOAT:
            return Double.class;
        case EIGHT_BYTE_FLOAT_SWAPPED:
            return Double.class;
        case TWO_BYTE_BCD:
            return Short.class;
        case FOUR_BYTE_BCD:
        case FOUR_BYTE_BCD_SWAPPED:
            return Integer.class;
        case CHAR:
        case VARCHAR:
            return String.class;
        }
        return null;
    }
}
