package com.serotonin.cdc.modbus4j.serial;

import com.serotonin.cdc.messaging.IncomingResponseMessage;
import com.serotonin.cdc.messaging.OutgoingRequestMessage;
import com.serotonin.cdc.messaging.WaitingRoomKey;
import com.serotonin.cdc.messaging.WaitingRoomKeyFactory;
import com.serotonin.cdc.modbus4j.msg.ModbusMessage;

public class SerialWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    private final int sync;

    public SerialWaitingRoomKeyFactory(int sync) {
        this.sync = sync;
    }

    //Override
    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return createWaitingRoomKey(((SerialMessage) request).getModbusMessage());
    }

    //Override
    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return createWaitingRoomKey(((SerialMessage) response).getModbusMessage());
    }

    private WaitingRoomKey createWaitingRoomKey(ModbusMessage msg) {
        if (sync == SerialMaster.SYNC_TRANSPORT)
            return new TransportSyncWaitingRoomKey();
        if (sync == SerialMaster.SYNC_SLAVE)
            return new SlaveSyncWaitingRoomKey(msg.getSlaveId());
        return new FunctionSyncWaitingRoomKey(msg.getSlaveId(), msg.getFunctionCode());
    }

    static class TransportSyncWaitingRoomKey implements WaitingRoomKey {
        //Override
        public int hashCode() {
            return 1;
        }

        //Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            return true;
        }
    }

    static class SlaveSyncWaitingRoomKey implements WaitingRoomKey {
        private final int slaveId;

        public SlaveSyncWaitingRoomKey(int slaveId) {
            this.slaveId = slaveId;
        }

        //Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + slaveId;
            return result;
        }

        //Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SlaveSyncWaitingRoomKey other = (SlaveSyncWaitingRoomKey) obj;
            if (slaveId != other.slaveId)
                return false;
            return true;
        }
    }

    static class FunctionSyncWaitingRoomKey implements WaitingRoomKey {
        private final int slaveId;
        private final byte functionCode;

        public FunctionSyncWaitingRoomKey(int slaveId, byte functionCode) {
            this.slaveId = slaveId;
            this.functionCode = functionCode;
        }

        //Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + functionCode;
            result = prime * result + slaveId;
            return result;
        }

        //Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FunctionSyncWaitingRoomKey other = (FunctionSyncWaitingRoomKey) obj;
            if (functionCode != other.functionCode)
                return false;
            if (slaveId != other.slaveId)
                return false;
            return true;
        }
    }
}
