package com.serotonin.modbus4j.sero.timer;

/**
 * An interface to abstract the source of current time away from System. This allows code to run in simulations where
 * the time is controlled explicitly.
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface TimeSource {
    /**
     * <p>currentTimeMillis.</p>
     *
     * @return a long.
     */
    long currentTimeMillis();
}
