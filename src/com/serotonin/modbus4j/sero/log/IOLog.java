/**
 * Copyright (C) 2014 Infinite Automation Software and Serotonin Software. All rights reserved.
 * @author Terry Packer, Matthew Lohbihler 
 */
package com.serotonin.modbus4j.sero.log;

import java.io.File;

public class IOLog extends BaseIOLog{
    //private static final Log LOG = LogFactory.getLog(IOLog.class);
    private static final int MAX_FILESIZE = 1000000;
    //    private static final int MAX_FILESIZE = 1000;
    private final File backupFile;

    public IOLog(String filename) {
    	super(new File(filename));
        backupFile = new File(filename + ".1");
    }

 
    @Override
    protected void sizeCheck() {
        // Check if the file should be rolled.
        if (file.length() > MAX_FILESIZE) {
            out.close();

            if (backupFile.exists())
                backupFile.delete();
            file.renameTo(backupFile);
            createOut();
        }
    }
    //
    //    public static void main(String[] args) {
    //        byte[] b = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    //
    //        IOLog log = new IOLog("iotest");
    //        log.log("test");
    //        log.log("testtest");
    //
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //        log.input(b);
    //        log.output(b);
    //
    //        log.log("testtesttesttesttesttesttesttesttesttest");
    //    }
}
