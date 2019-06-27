package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * First, instatiate with the streams. Then add a data consumer, or create a message control and pass this as the
 * transport (which will make the message control the data consumer). Change the read delay if desired. This class
 * supports running in its own thread (start) or an external one (run), say from a thread pool. Both approaches are
 * delegated to the stream listener. In either case, stop the transport with the stop method (or just stop the message
 * control).
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class StreamTransport implements Transport, Runnable {
    protected OutputStream out;
    protected InputStream in;
    private InputStreamListener listener;

    /**
     * <p>Constructor for StreamTransport.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @param out a {@link java.io.OutputStream} object.
     */
    public StreamTransport(InputStream in, OutputStream out) {
        this.out = out;
        this.in = in;
    }

    /**
     * <p>setReadDelay.</p>
     *
     * @param readDelay a int.
     */
    public void setReadDelay(int readDelay) {
        if (listener != null)
            listener.setReadDelay(readDelay);
    }

    /**
     * <p>start.</p>
     *
     * @param threadName a {@link java.lang.String} object.
     */
    public void start(String threadName) {
        listener.start(threadName);
    }

    /**
     * <p>stop.</p>
     */
    public void stop() {
        listener.stop();
    }

    /**
     * <p>run.</p>
     */
    public void run() {
        listener.run();
    }

    /** {@inheritDoc} */
    public void setConsumer(DataConsumer consumer) {
        listener = new InputStreamListener(in, consumer);
    }

    /**
     * <p>removeConsumer.</p>
     */
    public void removeConsumer() {
        listener.stop();
        listener = null;
    }

    /**
     * <p>write.</p>
     *
     * @param data an array of {@link byte} objects.
     * @throws java.io.IOException if any.
     */
    public void write(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    /** {@inheritDoc} */
    public void write(byte[] data, int len) throws IOException {
        out.write(data, 0, len);
        out.flush();
    }
}
