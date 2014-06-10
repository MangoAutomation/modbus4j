package com.serotonin.modbus4j.serial;

import com.serotonin.modbus4j.msg.ModbusMessage;

abstract public class SerialMessage {
    protected final ModbusMessage modbusMessage;

    public SerialMessage(ModbusMessage modbusMessage) {
        this.modbusMessage = modbusMessage;
    }

    public ModbusMessage getModbusMessage() {
        return modbusMessage;
    }

    @Override
    public String toString() {
        return "SerialMessage [modbusMessage=" + modbusMessage + "]";
    }
}
