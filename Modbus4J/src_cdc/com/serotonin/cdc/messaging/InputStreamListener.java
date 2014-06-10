package com.serotonin.cdc.messaging;

import java.io.IOException;
import java.io.InputStream;

import com.serotonin.cdc.util.StringUtils;

/**
 * This class provides a stoppable listener for an input stream that sends arbitrary information. A read() call to an
 * input stream will typically not return as long as the stream is not sending any data. This class provides a way for
 * stream listeners to safely listen and still respond when they are told to stop.
 * 
 * @author Matthew Lohbihler
 */
public class InputStreamListener implements Runnable {
    private static final int DEFAULT_READ_DELAY = 50;

    private final InputStream in;
    private final DataConsumer consumer;
    private volatile boolean running = true;

    /**
     * Defaulted to 20ms, this value tells the listener how long to wait between polls. Setting this to very small
     * values (as low as 1ms) can result in high processor consumption, but better responsiveness when data arrives in
     * the stream. Very high values have the opposite effect.
     */
    private int readDelay = DEFAULT_READ_DELAY;

    public InputStreamListener(InputStream in, DataConsumer consumer) {
        this.in = in;
        this.consumer = consumer;
    }

    public int getReadDelay() {
        return readDelay;
    }

    public void setReadDelay(int readDelay) {
        if (readDelay < 1)
            throw new IllegalArgumentException("readDelay cannot be less than one");
        this.readDelay = readDelay;
    }

    public void start(String threadName) {
        Thread thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        running = false;
        synchronized (this) {
            notify();
        }
    }

    public void run() {
        byte[] buf = new byte[1024];
        int readcount;
        try {
            while (running) {
                try {
                    if (in.available() == 0) {
                        synchronized (this) {
                            try {
                                wait(readDelay);
                            }
                            catch (InterruptedException e) {
                                // no op
                            }
                        }
                        continue;
                    }

                    readcount = in.read(buf);
                    consumer.data(buf, readcount);
                }
                catch (IOException e) {
                    if (StringUtils.equals(e.getMessage(), "Stream closed."))
                        break;
                    consumer.handleIOException(e);
                }
            }
        }
        finally {
            running = false;
        }
    }
}
