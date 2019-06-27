package com.serotonin.modbus4j.sero.messaging;


/**
 * <p>RequestHandler interface.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface RequestHandler {
    /**
     * Handle the request and return the appropriate response object.
     *
     * @param request
     *            the request to handle
     * @return the response object or null if no response is to be sent. null may also be returned if the request is
     *         handled asynchronously.
     * @throws java.lang.Exception if necessary
     */
    OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception;
}
