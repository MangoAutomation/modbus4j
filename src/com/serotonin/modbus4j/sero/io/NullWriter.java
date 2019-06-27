package com.serotonin.modbus4j.sero.io;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>NullWriter class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class NullWriter extends Writer {
    /** {@inheritDoc} */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        // no op
    }

    /** {@inheritDoc} */
    @Override
    public void flush() throws IOException {
        // no op
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        // no op
    }
}
