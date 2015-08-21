package com.serotonin.modbus4j.sero.epoll;

import java.io.IOException;

/**
 * A callback interface for input streams.
 * 
 * NOTE: if the InputStreamEPoll instance is terminated, any running processes will be destroyed without any
 * notification to this callback.
 * 
 * @author Matthew Lohbihler
 */
public interface Modbus4JInputStreamCallback {
    /**
     * Called when content is read from the input stream.
     * 
     * @param buf
     *            the content that was read. This is a shared byte array. Contents can be manipulated within this call,
     *            but the array itself should not be stored beyond the call since the contents will be changed.
     * @param len
     *            the length of content that was read.
     */
    void input(byte[] buf, int len);

    /**
     * Called when the closure of the input stream is detected.
     */
    void closed();

    /**
     * Called if there is an {@link IOException} while reading input stream.
     * 
     * @param e
     *            the exception that was received
     */
    void ioException(IOException e);

    /**
     * Called if the InputStreamEPoll instance was terminated while the input stream was still registered.
     */
    void terminated();
}
