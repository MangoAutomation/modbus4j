package com.serotonin.modbus4j.sero.timer;

/**
 * An implementation of TimeSource that returns the host time via System.
 * 
 * @author Matthew Lohbihler
 */
public class SystemTimeSource implements TimeSource {
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
