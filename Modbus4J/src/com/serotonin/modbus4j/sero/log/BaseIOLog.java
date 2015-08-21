/**
 * Copyright (C) 2014 Infinite Automation Software and Serotonin Software. All rights reserved.
 * @author Terry Packer, Matthew Lohbihler 
 */
package com.serotonin.modbus4j.sero.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.sero.io.NullWriter;
import com.serotonin.modbus4j.sero.io.StreamUtils;

/**
 * @author Terry Packer
 *
 */
public abstract class BaseIOLog {
    
	private static final Log LOG = LogFactory.getLog(BaseIOLog.class);
	
	protected static final String DATE_FORMAT = "yyyy/MM/dd-HH:mm:ss,SSS";
    protected final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    protected PrintWriter out;
    protected final File file;
    protected final StringBuilder sb = new StringBuilder();
    protected final Date date = new Date();
    
    public BaseIOLog(File logFile){
    	this.file = logFile;
        createOut();
    }
    
    /**
     * Create the Print Writer output
     */
    protected void createOut() {
        try {
            out = new PrintWriter(new FileWriter(file, true));
        }
        catch (IOException e) {
            out = new PrintWriter(new NullWriter());
            LOG.error("Error while creating process log", e);
        }
    }
	
    public void close() {
        out.close();
    }

    public void input(byte[] b) {
        log(true, b, 0, b.length);
    }

    public void input(byte[] b, int pos, int len) {
        log(true, b, pos, len);
    }

    public void output(byte[] b) {
        log(false, b, 0, b.length);
    }

    public void output(byte[] b, int pos, int len) {
        log(false, b, pos, len);
    }

    public void log(boolean input, byte[] b) {
        log(input, b, 0, b.length);
    }

    public synchronized void log(boolean input, byte[] b, int pos, int len) {
        sizeCheck();

        sb.delete(0, sb.length());
        date.setTime(System.currentTimeMillis());
        sb.append(sdf.format(date)).append(" ");
        sb.append(input ? "I" : "O").append(" ");
        sb.append(StreamUtils.dumpHex(b, pos, len));
        out.println(sb.toString());
        out.flush();
    }

    public synchronized void log(String message) {
        sizeCheck();

        sb.delete(0, sb.length());
        date.setTime(System.currentTimeMillis());
        sb.append(sdf.format(date)).append(" ");
        sb.append(message);
        out.println(sb.toString());
        out.flush();
    }
    
    /**
     * Check the size of the logfile and perform adjustments
     * as necessary
     */
    protected abstract void sizeCheck();
}
