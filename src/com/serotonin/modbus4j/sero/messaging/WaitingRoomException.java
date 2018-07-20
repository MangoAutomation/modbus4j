package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

public class WaitingRoomException extends IOException {
    private static final long serialVersionUID = 1L;

    public WaitingRoomException(String message) {
        super(message);
    }
}
