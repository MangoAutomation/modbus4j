/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.sero;

/**
 * @author Matthew Lohbihler
 */
public class NotImplementedException extends RuntimeException {
    static final long serialVersionUID = -1;

    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String message) {
        super(message);
    }
}
