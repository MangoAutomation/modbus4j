package com.serotonin.cdc.messaging;

import java.io.IOException;

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

    public void handleIOException(IOException e);
}
