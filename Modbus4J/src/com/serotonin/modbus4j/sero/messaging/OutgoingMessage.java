package com.serotonin.modbus4j.sero.messaging;

public interface OutgoingMessage {
    /**
     * Return the byte array representing the serialization of the request.
     * 
     * @return byte array representing the serialization of the request
     */
    byte[] getMessageData();
}
