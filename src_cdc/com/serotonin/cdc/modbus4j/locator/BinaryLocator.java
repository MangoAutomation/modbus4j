package com.serotonin.cdc.modbus4j.locator;

import com.serotonin.cdc.NotImplementedException;
import com.serotonin.cdc.modbus4j.base.ModbusUtils;
import com.serotonin.cdc.modbus4j.code.DataType;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.ModbusIdException;

public class BinaryLocator extends BaseLocator {
    private int bit = -1;

    public BinaryLocator(int slaveId, int range, int offset) {
        super(slaveId, range, offset);
        if (!isBinaryRange(range))
            throw new ModbusIdException("Non-bit requests can only be made from coil status and input status ranges");
        validate();
    }

    public BinaryLocator(int slaveId, int range, int offset, int bit) {
        super(slaveId, range, offset);
        if (isBinaryRange(range))
            throw new ModbusIdException("Bit requests can only be made from holding registers and input registers");
        this.bit = bit;
        validate();
    }

    public static boolean isBinaryRange(int range) {
        return range == RegisterRange.COIL_STATUS || range == RegisterRange.INPUT_STATUS;
    }

    protected void validate() {
        super.validate(1);

        if (!isBinaryRange(range))
            ModbusUtils.validateBit(bit);
    }

    public int getBit() {
        return bit;
    }

    //Override
    public int getDataType() {
        return DataType.BINARY;
    }

    //Override
    public int getRegisterCount() {
        return 1;
    }

    //Override
    public String toString() {
        return "BinaryLocator(slaveId=" + getSlaveId() + ", range=" + range + ", offset=" + offset + ", bit=" + bit
                + ")";
    }

    //Override
    public Object bytesToValueRealOffset(byte[] data, int offset) {
        // If this is a coil or input, convert to boolean.
        if (range == RegisterRange.COIL_STATUS || range == RegisterRange.INPUT_STATUS)
            return new Boolean((((data[offset / 8] & 0xff) >> (offset % 8)) & 0x1) == 1);

        // For the rest of the types, we double the normalized offset to account for short to byte.
        offset *= 2;

        // We could still be asking for a binary if it's a bit in a register.
        return new Boolean((((data[offset + 1 - bit / 8] & 0xff) >> (bit % 8)) & 0x1) == 1);
    }

    //Override
    public short[] valueToShorts(Object value) {
        throw new NotImplementedException();
    }
}
