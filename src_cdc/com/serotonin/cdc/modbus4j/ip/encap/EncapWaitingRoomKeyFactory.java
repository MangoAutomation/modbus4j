package com.serotonin.cdc.modbus4j.ip.encap;

import com.serotonin.cdc.messaging.IncomingResponseMessage;
import com.serotonin.cdc.messaging.OutgoingRequestMessage;
import com.serotonin.cdc.messaging.WaitingRoomKey;
import com.serotonin.cdc.messaging.WaitingRoomKeyFactory;
import com.serotonin.cdc.modbus4j.ip.IpMessage;
import com.serotonin.cdc.modbus4j.msg.ModbusMessage;

public class EncapWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    //Override
    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return createWaitingRoomKey(((IpMessage) request).getModbusMessage());
    }

    //Override
    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return createWaitingRoomKey(((IpMessage) response).getModbusMessage());
    }

    public WaitingRoomKey createWaitingRoomKey(ModbusMessage msg) {
        return new EncapWaitingRoomKey(msg.getSlaveId(), msg.getFunctionCode());
    }

    class EncapWaitingRoomKey implements WaitingRoomKey {
        private final int slaveId;
        private final byte functionCode;

        public EncapWaitingRoomKey(int slaveId, byte functionCode) {
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
            EncapWaitingRoomKey other = (EncapWaitingRoomKey) obj;
            if (functionCode != other.functionCode)
                return false;
            if (slaveId != other.slaveId)
                return false;
            return true;
        }
    }
}
