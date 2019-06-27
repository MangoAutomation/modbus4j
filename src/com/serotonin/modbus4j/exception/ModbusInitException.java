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
package com.serotonin.modbus4j.exception;

/**
 * <p>ModbusInitException class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ModbusInitException extends Exception {
    private static final long serialVersionUID = -1;

    /**
     * <p>Constructor for ModbusInitException.</p>
     */
    public ModbusInitException() {
        super();
    }

    /**
     * <p>Constructor for ModbusInitException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param cause a {@link java.lang.Throwable} object.
     */
    public ModbusInitException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <p>Constructor for ModbusInitException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public ModbusInitException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for ModbusInitException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public ModbusInitException(Throwable cause) {
        super(cause);
    }
}
