package com.serotonin.modbus4j.sero.messaging;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides synchronization on the input stream read by wrapping it.
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class TestableTransport extends StreamTransport {
    /**
     * <p>Constructor for TestableTransport.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @param out a {@link java.io.OutputStream} object.
     */
    public TestableTransport(InputStream in, OutputStream out) {
        super(new TestableBufferedInputStream(in), out);
    }

    /**
     * <p>testInputStream.</p>
     *
     * @throws java.io.IOException if any.
     */
    public void testInputStream() throws IOException {
        ((TestableBufferedInputStream) in).test();
    }

    static class TestableBufferedInputStream extends BufferedInputStream {
        public TestableBufferedInputStream(InputStream in) {
            super(in);
        }

        @Override
        public synchronized int read(byte[] buf) throws IOException {
            return super.read(buf);
        }

        public synchronized void test() throws IOException {
            mark(1);
            int i = read();
            if (i == -1)
                throw new IOException("Stream closed");
            reset();
        }
    }
}
