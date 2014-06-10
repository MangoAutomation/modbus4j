package com.serotonin.cdc.messaging;

public interface RequestHandler {
    /**
     * Handle the request and return the appropriate response object.
     * 
     * @param request
     *            the request to handle
     * @return the response object or null if no response is to be sent. null may also be returned if the request is
     *         handled asynchronously.
     * @throws Exception
     */
    OutgoingResponseMessage handleRequest(IncomingRequestMessage request) throws Exception;
}
