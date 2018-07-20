package com.serotonin.cdc.modbus4j.locator;

import com.serotonin.cdc.modbus4j.code.DataType;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.IllegalDataTypeException;

public class StringLocator extends BaseLocator {
    public static final String ASCII = "ASCII";

    private final int dataType;
    private final int registerCount;
    private final String charset;

    public StringLocator(int slaveId, int range, int offset, int dataType, int registerCount) {
        this(slaveId, range, offset, dataType, registerCount, ASCII);
    }

    public StringLocator(int slaveId, int range, int offset, int dataType, int registerCount, String charset) {
        super(slaveId, range, offset);
        this.dataType = dataType;
        this.registerCount = registerCount;
        this.charset = charset;
        validate();
    }

    private void validate() {
        super.validate(registerCount);

        if (range == RegisterRange.COIL_STATUS || range == RegisterRange.INPUT_STATUS)
            throw new IllegalDataTypeException("Only binary values can be read from Coil and Input ranges");

        if (dataType != DataType.CHAR && dataType != DataType.VARCHAR)
            throw new IllegalDataTypeException("Invalid data type");
    }

    //Override
    public int getDataType() {
        return dataType;
    }

    //Override
    public int getRegisterCount() {
        return registerCount;
    }

    //Override
    public String toString() {
        return "StringLocator(slaveId=" + getSlaveId() + ", range=" + range + ", offset=" + offset + ", dataType="
                + dataType + ", registerCount=" + registerCount + ", charset=" + charset + ")";
    }

    //Override
    public Object bytesToValueRealOffset(byte[] data, int offset) {
        offset *= 2;
        int length = registerCount * 2;

        try {
            if (dataType == DataType.CHAR)
                return new String(data, offset, length, charset);

            if (dataType == DataType.VARCHAR) {
                int nullPos = -1;
                for (int i = 0; i < length; i++) {
                    if (data[i] == 0) {
                        nullPos = i;
                        break;
                    }
                }

                if (nullPos == -1)
                    return new String(data, offset, length, charset);
                return new String(data, offset, nullPos, charset);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Unsupported encoding exception: " + charset);
        }

        throw new RuntimeException("Unsupported data type: " + dataType);
    }

    //Override
    public short[] valueToShorts(Object val) {
        String value = (String) val;
        short[] result = new short[registerCount];
        int resultByteLen = registerCount * 2;

        int length;
        if (value != null) {
            byte[] bytes;
            try {
                bytes = value.getBytes(charset);
            }
            //catch (UnsupportedEncodingException e) { GWatson 26-07-2011 not available in AJ102 runtime
            catch (Exception e) {
                throw new RuntimeException("Unsupported encoding exception: " + charset);
            }

            length = resultByteLen;
            if (length > bytes.length)
                length = bytes.length;

            for (int i = 0; i < length; i++)
                setByte(result, i, bytes[i] & 0xff);
        }
        else
            length = 0;

        if (dataType == DataType.CHAR) {
            // Pad the rest with spaces
            for (int i = length; i < resultByteLen; i++)
                setByte(result, i, 0x20);
        }
        else if (dataType == DataType.VARCHAR) {
            if (length >= resultByteLen)
                // Ensure the last byte is a null terminator.
                result[registerCount - 1] &= 0xff00;
            else {
                // Pad the rest with null.
                for (int i = length; i < resultByteLen; i++)
                    setByte(result, i, 0);
            }
        }
        else
            throw new RuntimeException("Unsupported data type: " + dataType);

        return result;
    }

    private void setByte(short[] s, int byteIndex, int value) {
        if (byteIndex % 2 == 0)
            s[byteIndex / 2] |= value << 8;
        else
            s[byteIndex / 2] |= value;
    }
}
