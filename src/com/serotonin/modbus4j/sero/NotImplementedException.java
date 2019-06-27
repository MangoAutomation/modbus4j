/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.sero;

/**
 * <p>NotImplementedException class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class NotImplementedException extends RuntimeException {
    static final long serialVersionUID = -1;

    /**
     * <p>Constructor for NotImplementedException.</p>
     */
    public NotImplementedException() {
        super();
    }

    /**
     * <p>Constructor for NotImplementedException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public NotImplementedException(String message) {
        super(message);
    }
}
