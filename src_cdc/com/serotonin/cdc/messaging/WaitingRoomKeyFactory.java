package com.serotonin.cdc.messaging;

public interface WaitingRoomKeyFactory {
    WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request);

    WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response);
}
