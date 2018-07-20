package com.serotonin.cdc.messaging;

public class DefaultMessagingExceptionHandler implements MessagingExceptionHandler {
    public void receivedException(Exception e) {
        e.printStackTrace();
    }
}
