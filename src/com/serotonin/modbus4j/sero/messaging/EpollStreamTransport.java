package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.serotonin.modbus4j.sero.epoll.InputStreamEPollWrapper;
import com.serotonin.modbus4j.sero.epoll.Modbus4JInputStreamCallback;

/**
 * First, instatiate with the streams and epoll. Then add a data consumer, or create a message control and pass this as
 * the transport (which will make the message control the data consumer). Stop the transport by stopping the message
 * control).
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class EpollStreamTransport implements Transport {
    private final OutputStream out;
    private final InputStream in;
    private final InputStreamEPollWrapper epoll;

    /**
     * <p>Constructor for EpollStreamTransport.</p>
     *
     * @param in a {@link java.io.InputStream} object.
     * @param out a {@link java.io.OutputStream} object.
     * @param epoll a {@link com.serotonin.modbus4j.sero.epoll.InputStreamEPollWrapper} object.
     */
    public EpollStreamTransport(InputStream in, OutputStream out, InputStreamEPollWrapper epoll) {
        this.out = out;
        this.in = in;
        this.epoll = epoll;
    }

    /** {@inheritDoc} */
    @Override
    public void setConsumer(final DataConsumer consumer) {
        epoll.add(in, new Modbus4JInputStreamCallback() {
            @Override
            public void terminated() {
                removeConsumer();
            }

            @Override
            public void ioException(IOException e) {
                consumer.handleIOException(e);
            }

            @Override
            public void input(byte[] buf, int len) {
                consumer.data(buf, len);
            }

            @Override
            public void closed() {
                removeConsumer();
            }
        });
    }

    /**
     * <p>removeConsumer.</p>
     */
    @Override
    public void removeConsumer() {
        epoll.remove(in);
    }

    /**
     * <p>write.</p>
     *
     * @param data an array of {@link byte} objects.
     * @throws java.io.IOException if any.
     */
    @Override
    public void write(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void write(byte[] data, int len) throws IOException {
        out.write(data, 0, len);
        out.flush();
    }
}
