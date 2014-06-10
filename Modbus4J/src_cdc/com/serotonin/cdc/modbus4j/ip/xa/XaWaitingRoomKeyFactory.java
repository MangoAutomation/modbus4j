package com.serotonin.cdc.modbus4j.ip.xa;

import com.serotonin.cdc.messaging.IncomingResponseMessage;
import com.serotonin.cdc.messaging.OutgoingRequestMessage;
import com.serotonin.cdc.messaging.WaitingRoomKey;
import com.serotonin.cdc.messaging.WaitingRoomKeyFactory;
import com.serotonin.cdc.modbus4j.msg.ModbusMessage;

public class XaWaitingRoomKeyFactory implements WaitingRoomKeyFactory {
    //Override
    public WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request) {
        return createWaitingRoomKey((XaMessage) request);
    }

    //Override
    public WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response) {
        return createWaitingRoomKey((XaMessage) response);
    }

    public WaitingRoomKey createWaitingRoomKey(XaMessage msg) {
        return new XaWaitingRoomKey(msg.getTransactionId(), msg.getModbusMessage());
    }

    class XaWaitingRoomKey implements WaitingRoomKey {
        private final int transactionId;
        private final int slaveId;
        private final byte functionCode;

        public XaWaitingRoomKey(int transactionId, ModbusMessage msg) {
            this.transactionId = transactionId;
            this.slaveId = msg.getSlaveId();
            this.functionCode = msg.getFunctionCode();
        }

        //Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + functionCode;
            result = prime * result + slaveId;
            result = prime * result + transactionId;
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
            XaWaitingRoomKey other = (XaWaitingRoomKey) obj;
            if (functionCode != other.functionCode)
                return false;
            if (slaveId != other.slaveId)
                return false;
            if (transactionId != other.transactionId)
                return false;
            return true;
        }
    }
}
