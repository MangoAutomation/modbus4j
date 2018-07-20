package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.serotonin.modbus4j.sero.epoll.Modbus4JInputStreamCallback;
import com.serotonin.modbus4j.sero.epoll.InputStreamEPollWrapper;

/**
 * First, instatiate with the streams and epoll. Then add a data consumer, or create a message control and pass this as
 * the transport (which will make the message control the data consumer). Stop the transport by stopping the message
 * control).
 * 
 * @author Matthew Lohbihler
 */
public class EpollStreamTransport implements Transport {
    private final OutputStream out;
    private final InputStream in;
    private final InputStreamEPollWrapper epoll;

    public EpollStreamTransport(InputStream in, OutputStream out, InputStreamEPollWrapper epoll) {
        this.out = out;
        this.in = in;
        this.epoll = epoll;
    }

    public void setConsumer(final DataConsumer consumer) {
        epoll.add(in, new Modbus4JInputStreamCallback() {
            public void terminated() {
                removeConsumer();
            }

            public void ioException(IOException e) {
                consumer.handleIOException(e);
            }

            public void input(byte[] buf, int len) {
                consumer.data(buf, len);
            }

            public void closed() {
                removeConsumer();
            }
        });
    }

    public void removeConsumer() {
        epoll.remove(in);
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
