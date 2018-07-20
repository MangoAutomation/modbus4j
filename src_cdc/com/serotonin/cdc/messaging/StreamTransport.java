package com.serotonin.cdc.messaging;

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
 */
public class StreamTransport implements Transport, Runnable {
    protected OutputStream out;
    protected InputStream in;
    private InputStreamListener listener;

    public StreamTransport(InputStream in, OutputStream out) {
        this.out = out;
        this.in = in;
    }

    public void setReadDelay(int readDelay) {
        if (listener != null)
            listener.setReadDelay(readDelay);
    }

    public void start(String threadName) {
        listener.start(threadName);
    }

    public void stop() {
        listener.stop();
    }

    public void run() {
        listener.run();
    }

    public void setConsumer(DataConsumer consumer) {
        listener = new InputStreamListener(in, consumer);
    }

    public void removeConsumer() {
        listener.stop();
        listener = null;
    }

    public void write(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    public void write(byte[] data, int len) throws IOException {
        out.write(data, 0, len);
        out.flush();
    }
}
