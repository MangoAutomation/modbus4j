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
package com.serotonin.cdc.modbus4j.code;

/**
 * @author Matthew Lohbihler
 */
public class FunctionCode {
    public static final byte READ_COILS = 1;
    public static final byte READ_DISCRETE_INPUTS = 2;
    public static final byte READ_HOLDING_REGISTERS = 3;
    public static final byte READ_INPUT_REGISTERS = 4;
    public static final byte WRITE_COIL = 5;
    public static final byte WRITE_REGISTER = 6;
    public static final byte READ_EXCEPTION_STATUS = 7;
    public static final byte WRITE_COILS = 15;
    public static final byte WRITE_REGISTERS = 16;
    public static final byte REPORT_SLAVE_ID = 17;
    public static final byte WRITE_MASK_REGISTER = 22;

    public static String toString(byte code) {
        return Integer.toString(code & 0xff);
    }
}
