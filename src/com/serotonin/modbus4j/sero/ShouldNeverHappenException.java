/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.sero;

/**
 * <p>ShouldNeverHappenException class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ShouldNeverHappenException extends RuntimeException {
    private static final long serialVersionUID = -1;
    
    /**
     * <p>Constructor for ShouldNeverHappenException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public ShouldNeverHappenException(String message) {
        super(message);
    }

    /**
     * <p>Constructor for ShouldNeverHappenException.</p>
     *
     * @param cause a {@link java.lang.Throwable} object.
     */
    public ShouldNeverHappenException(Throwable cause) {
        super(cause);
    }
}
