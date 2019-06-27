package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

/**
 * <p>DataConsumer interface.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface DataConsumer {
    /**
     * Notifies the consumer that new data is available
     *
     * @param b
     *            array of bytes representing the incoming information
     * @param len
     *            length of the data
     */
    public void data(byte[] b, int len);

    /**
     * <p>handleIOException.</p>
     *
     * @param e a {@link java.io.IOException} object.
     */
    public void handleIOException(IOException e);
}
