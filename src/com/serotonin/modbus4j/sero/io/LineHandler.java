/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.sero.io;

/**
 * <p>LineHandler interface.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface LineHandler {
    /**
     * <p>handleLine.</p>
     *
     * @param line a {@link java.lang.String} object.
     */
    public void handleLine(String line);

    /**
     * <p>done.</p>
     */
    public void done();
}
