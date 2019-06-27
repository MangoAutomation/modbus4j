package com.serotonin.modbus4j.serial;

import com.serotonin.modbus4j.msg.ModbusMessage;

/**
 * <p>Abstract SerialMessage class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
abstract public class SerialMessage {
    protected final ModbusMessage modbusMessage;

    /**
     * <p>Constructor for SerialMessage.</p>
     *
     * @param modbusMessage a {@link com.serotonin.modbus4j.msg.ModbusMessage} object.
     */
    public SerialMessage(ModbusMessage modbusMessage) {
        this.modbusMessage = modbusMessage;
    }

    /**
     * <p>Getter for the field <code>modbusMessage</code>.</p>
     *
     * @return a {@link com.serotonin.modbus4j.msg.ModbusMessage} object.
     */
    public ModbusMessage getModbusMessage() {
        return modbusMessage;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "SerialMessage [modbusMessage=" + modbusMessage + "]";
    }
}
