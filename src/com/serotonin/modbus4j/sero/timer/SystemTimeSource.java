package com.serotonin.modbus4j.sero.timer;

/**
 * An implementation of TimeSource that returns the host time via System.
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class SystemTimeSource implements TimeSource {
    /**
     * <p>currentTimeMillis.</p>
     *
     * @return a long.
     */
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
