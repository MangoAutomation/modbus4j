package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

/**
 * A transport is a wrapper around the means by which data is transferred. So, there could be transports for serial
 * ports, sockets, UDP, email, etc.
 * 
 * @author Matthew Lohbihler
 */
public interface Transport {
    abstract void setConsumer(DataConsumer consumer) throws IOException;

    abstract void removeConsumer();

    abstract void write(byte[] data) throws IOException;

    abstract void write(byte[] data, int len) throws IOException;
}
