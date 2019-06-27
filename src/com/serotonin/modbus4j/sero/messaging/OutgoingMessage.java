package com.serotonin.modbus4j.sero.messaging;

/**
 * <p>OutgoingMessage interface.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface OutgoingMessage {
    /**
     * Return the byte array representing the serialization of the request.
     *
     * @return byte array representing the serialization of the request
     */
    byte[] getMessageData();
}
