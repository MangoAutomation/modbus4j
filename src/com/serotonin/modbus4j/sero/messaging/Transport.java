package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

/**
 * A transport is a wrapper around the means by which data is transferred. So, there could be transports for serial
 * ports, sockets, UDP, email, etc.
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface Transport {
    /**
     * <p>setConsumer.</p>
     *
     * @param consumer a {@link com.serotonin.modbus4j.sero.messaging.DataConsumer} object.
     * @throws java.io.IOException if any.
     */
    abstract void setConsumer(DataConsumer consumer) throws IOException;

    /**
     * <p>removeConsumer.</p>
     */
    abstract void removeConsumer();

    /**
     * <p>write.</p>
     *
     * @param data an array of {@link byte} objects.
     * @throws java.io.IOException if any.
     */
    abstract void write(byte[] data) throws IOException;

    /**
     * <p>write.</p>
     *
     * @param data an array of {@link byte} objects.
     * @param len a int.
     * @throws java.io.IOException if any.
     */
    abstract void write(byte[] data, int len) throws IOException;
}
