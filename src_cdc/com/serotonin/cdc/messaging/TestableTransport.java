package com.serotonin.cdc.messaging;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Provides synchronization on the input stream read by wrapping it.
 * 
 * @author Matthew Lohbihler
 */
public class TestableTransport extends StreamTransport {
    public TestableTransport(InputStream in, OutputStream out) {
        super(new TestableBufferedInputStream(in), out);
    }

    public void testInputStream() throws IOException {
        ((TestableBufferedInputStream) in).test();
    }

    static class TestableBufferedInputStream extends BufferedInputStream {
        public TestableBufferedInputStream(InputStream in) {
            super(in);
        }

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
