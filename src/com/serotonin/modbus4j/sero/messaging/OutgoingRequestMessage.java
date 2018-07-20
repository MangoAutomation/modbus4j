package com.serotonin.modbus4j.sero.messaging;


public interface OutgoingRequestMessage extends OutgoingMessage {
    /**
     * Whether the request is expecting a response or not.
     * 
     * @return true if a response is expected, false otherwise.
     */
    boolean expectsResponse();
}
