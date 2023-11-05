package com.serotonin.modbus4j.ip.rtu;

import com.serotonin.modbus4j.sero.messaging.IncomingResponseMessage;
import com.serotonin.modbus4j.sero.messaging.OutgoingRequestMessage;
import com.serotonin.modbus4j.sero.messaging.WaitingRoomKey;
import com.serotonin.modbus4j.sero.messaging.WaitingRoomKeyFactory;

public class Tcp2RtuSerialWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    private  final Sync sync = new Sync();

    public Tcp2RtuSerialWaitingRoomKeyFactory() {
    }

    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return sync;
    }

    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return sync;
    }

    static class Sync implements WaitingRoomKey {
        Sync() {
        }

        public int hashCode() {
            return 1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (obj == null) {
                return false;
            } else {
                return this.getClass() == obj.getClass();
            }
        }
    }
}
