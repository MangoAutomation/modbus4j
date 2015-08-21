package com.serotonin.modbus4j.sero.io;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        // no op
    }

    @Override
    public void flush() throws IOException {
        // no op
    }

    @Override
    public void close() throws IOException {
        // no op
    }
}
