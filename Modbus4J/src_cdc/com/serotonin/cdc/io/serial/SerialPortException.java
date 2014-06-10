/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.

    This program is free software; you can redistribute it and/or modify
    it under the terms of version 2 of the GNU General Public License as 
    published by the Free Software Foundation and additional terms as 
    specified by Serotonin Software Technologies Inc.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

 	@author Matthew Lohbihler
 */

package com.serotonin.cdc.io.serial;

/**
 * @author Matthew Lohbihler
 *
 */
public class SerialPortException extends Exception {
    private static final long serialVersionUID = -1;
    
    public SerialPortException(String message) {
        super(message);
    }

    public SerialPortException(Throwable cause) {
        super(cause);
    }
}
