package com.serotonin.cdc.modbus4j.locator;

import java.math.BigInteger;

import com.serotonin.cdc.modbus4j.code.DataType;
import com.serotonin.cdc.modbus4j.code.RegisterRange;
import com.serotonin.cdc.modbus4j.exception.IllegalDataTypeException;
import com.serotonin.cdc.util.ArrayUtils;

public class NumericLocator extends BaseLocator {
    private static final int[] DATA_TYPES = { //
    DataType.TWO_BYTE_INT_UNSIGNED, //
            DataType.TWO_BYTE_INT_SIGNED, //
            DataType.FOUR_BYTE_INT_UNSIGNED, //
            DataType.FOUR_BYTE_INT_SIGNED, //
            DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED, //
            DataType.FOUR_BYTE_INT_SIGNED_SWAPPED, //
            DataType.FOUR_BYTE_FLOAT, //
            DataType.FOUR_BYTE_FLOAT_SWAPPED, //
            DataType.EIGHT_BYTE_INT_UNSIGNED, //
            DataType.EIGHT_BYTE_INT_SIGNED, //
            DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED, //
            DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED, //
            DataType.EIGHT_BYTE_FLOAT, //
            DataType.EIGHT_BYTE_FLOAT_SWAPPED, //
            DataType.TWO_BYTE_BCD, //
            DataType.FOUR_BYTE_BCD, //
    };

    private final int dataType;

    public NumericLocator(int slaveId, int range, int offset, int dataType) {
        super(slaveId, range, offset);
        this.dataType = dataType;
        validate();
    }

    private void validate() {
        super.validate(getRegisterCount());

        if (range == RegisterRange.COIL_STATUS || range == RegisterRange.INPUT_STATUS)
            throw new IllegalDataTypeException("Only binary values can be read from Coil and Input ranges");

        if (!ArrayUtils.contains(DATA_TYPES, dataType))
            throw new IllegalDataTypeException("Invalid data type");
    }

    //Override
    public int getDataType() {
        return dataType;
    }

    //Override
    public String toString() {
        return "NumericLocator(slaveId=" + getSlaveId() + ", range=" + range + ", offset=" + offset + ", dataType="
                + dataType + ")";
    }

    //Override
    public int getRegisterCount() {
        switch (dataType) {
        case DataType.TWO_BYTE_INT_UNSIGNED:
        case DataType.TWO_BYTE_INT_SIGNED:
        case DataType.TWO_BYTE_BCD:
            return 1;
        case DataType.FOUR_BYTE_INT_UNSIGNED:
        case DataType.FOUR_BYTE_INT_SIGNED:
        case DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED:
        case DataType.FOUR_BYTE_INT_SIGNED_SWAPPED:
        case DataType.FOUR_BYTE_FLOAT:
        case DataType.FOUR_BYTE_FLOAT_SWAPPED:
        case DataType.FOUR_BYTE_BCD:
            return 2;
        case DataType.EIGHT_BYTE_INT_UNSIGNED:
        case DataType.EIGHT_BYTE_INT_SIGNED:
        case DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED:
        case DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED:
        case DataType.EIGHT_BYTE_FLOAT:
        case DataType.EIGHT_BYTE_FLOAT_SWAPPED:
            return 4;
        }

        throw new RuntimeException("Unsupported data type: " + dataType);
    }

    //Override
    public Object bytesToValueRealOffset(byte[] data, int offset) {
        offset *= 2;

        // 2 bytes
        if (dataType == DataType.TWO_BYTE_INT_UNSIGNED)
            return new Integer(((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff));

        if (dataType == DataType.TWO_BYTE_INT_SIGNED)
            return new Short((short) (((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff)));

        if (dataType == DataType.TWO_BYTE_BCD) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 2; i++) {
                sb.append(bcdNibbleToInt(data[offset + i], true));
                sb.append(bcdNibbleToInt(data[offset + i], false));
            }
            return new Short(Short.parseShort(sb.toString()));
        }

        // 4 bytes
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED)
            return new Long(((long) ((data[offset] & 0xff)) << 24) | ((long) ((data[offset + 1] & 0xff)) << 16)
                    | ((long) ((data[offset + 2] & 0xff)) << 8) | ((data[offset + 3] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_INT_SIGNED)
            return new Integer(((data[offset] & 0xff) << 24) | ((data[offset + 1] & 0xff) << 16)
                    | ((data[offset + 2] & 0xff) << 8) | (data[offset + 3] & 0xff));

        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED)
            return new Long(((long) ((data[offset + 2] & 0xff)) << 24) | ((long) ((data[offset + 3] & 0xff)) << 16)
                    | ((long) ((data[offset] & 0xff)) << 8) | ((data[offset + 1] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED)
            return new Integer(((data[offset + 2] & 0xff) << 24) | ((data[offset + 3] & 0xff) << 16)
                    | ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff));

        if (dataType == DataType.FOUR_BYTE_FLOAT)
            return new Float(Float.intBitsToFloat(((data[offset] & 0xff) << 24) | ((data[offset + 1] & 0xff) << 16)
                    | ((data[offset + 2] & 0xff) << 8) | (data[offset + 3] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_FLOAT_SWAPPED)
            return new Float(Float.intBitsToFloat(((data[offset + 2] & 0xff) << 24) | ((data[offset + 3] & 0xff) << 16)
                    | ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff)));

        if (dataType == DataType.FOUR_BYTE_BCD) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 4; i++) {
                sb.append(bcdNibbleToInt(data[offset + i], true));
                sb.append(bcdNibbleToInt(data[offset + i], false));
            }
            return new Integer(Integer.parseInt(sb.toString()));
        }

        // 8 bytes
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED) {
            byte[] b9 = new byte[9];
            System.arraycopy(data, offset, b9, 1, 8);
            return new BigInteger(b9);
        }

        if (dataType == DataType.EIGHT_BYTE_INT_SIGNED)
            return new Long(((long) ((data[offset] & 0xff)) << 56) | ((long) ((data[offset + 1] & 0xff)) << 48)
                    | ((long) ((data[offset + 2] & 0xff)) << 40) | ((long) ((data[offset + 3] & 0xff)) << 32)
                    | ((long) ((data[offset + 4] & 0xff)) << 24) | ((long) ((data[offset + 5] & 0xff)) << 16)
                    | ((long) ((data[offset + 6] & 0xff)) << 8) | ((data[offset + 7] & 0xff)));

        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED) {
            byte[] b9 = new byte[9];
            b9[1] = data[offset + 6];
            b9[2] = data[offset + 7];
            b9[3] = data[offset + 4];
            b9[4] = data[offset + 5];
            b9[5] = data[offset + 2];
            b9[6] = data[offset + 3];
            b9[7] = data[offset];
            b9[8] = data[offset + 1];
            return new BigInteger(b9);
        }

        if (dataType == DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED)
            return new Long(((long) ((data[offset + 6] & 0xff)) << 56) | ((long) ((data[offset + 7] & 0xff)) << 48)
                    | ((long) ((data[offset + 4] & 0xff)) << 40) | ((long) ((data[offset + 5] & 0xff)) << 32)
                    | ((long) ((data[offset + 2] & 0xff)) << 24) | ((long) ((data[offset + 3] & 0xff)) << 16)
                    | ((long) ((data[offset] & 0xff)) << 8) | ((data[offset + 1] & 0xff)));

        if (dataType == DataType.EIGHT_BYTE_FLOAT)
            return new Double(Double.longBitsToDouble(((long) ((data[offset] & 0xff)) << 56)
                    | ((long) ((data[offset + 1] & 0xff)) << 48) | ((long) ((data[offset + 2] & 0xff)) << 40)
                    | ((long) ((data[offset + 3] & 0xff)) << 32) | ((long) ((data[offset + 4] & 0xff)) << 24)
                    | ((long) ((data[offset + 5] & 0xff)) << 16) | ((long) ((data[offset + 6] & 0xff)) << 8)
                    | ((data[offset + 7] & 0xff))));

        if (dataType == DataType.EIGHT_BYTE_FLOAT_SWAPPED)
            return new Double(Double.longBitsToDouble(((long) ((data[offset + 6] & 0xff)) << 56)
                    | ((long) ((data[offset + 7] & 0xff)) << 48) | ((long) ((data[offset + 4] & 0xff)) << 40)
                    | ((long) ((data[offset + 5] & 0xff)) << 32) | ((long) ((data[offset + 2] & 0xff)) << 24)
                    | ((long) ((data[offset + 3] & 0xff)) << 16) | ((long) ((data[offset] & 0xff)) << 8)
                    | ((data[offset + 1] & 0xff))));

        throw new RuntimeException("Unsupported data type: " + dataType);
    }

    private static int bcdNibbleToInt(byte b, boolean high) {
        int n;
        if (high)
            n = (b >> 4) & 0xf;
        else
            n = b & 0xf;
        if (n > 9)
            n = 0;
        return n;
    }

    //Override
    public short[] valueToShorts(Object val) {
        Number value = (Number) val;
        // 2 bytes
        if (dataType == DataType.TWO_BYTE_INT_UNSIGNED || dataType == DataType.TWO_BYTE_INT_SIGNED)
            return new short[] { value.shortValue() };

        if (dataType == DataType.TWO_BYTE_BCD) {
            short s = value.shortValue();
            return new short[] { (short) ((((s / 1000) % 10) << 12) | (((s / 100) % 10) << 8) | (((s / 10) % 10) << 4) | (s % 10)) };
        }

        // 4 bytes
        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED || dataType == DataType.FOUR_BYTE_INT_SIGNED) {
            int i = value.intValue();
            return new short[] { (short) (i >> 16), (short) i };
        }

        if (dataType == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED || dataType == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED) {
            int i = value.intValue();
            return new short[] { (short) i, (short) (i >> 16) };
        }

        if (dataType == DataType.FOUR_BYTE_FLOAT) {
            int i = Float.floatToIntBits(value.floatValue());
            return new short[] { (short) (i >> 16), (short) i };
        }

        if (dataType == DataType.FOUR_BYTE_FLOAT_SWAPPED) {
            int i = Float.floatToIntBits(value.floatValue());
            return new short[] { (short) i, (short) (i >> 16) };
        }

        if (dataType == DataType.FOUR_BYTE_BCD) {
            int i = value.intValue();
            return new short[] {
                    (short) ((((i / 10000000) % 10) << 12) | (((i / 1000000) % 10) << 8) | (((i / 100000) % 10) << 4) | ((i / 10000) % 10)),
                    (short) ((((i / 1000) % 10) << 12) | (((i / 100) % 10) << 8) | (((i / 10) % 10) << 4) | (i % 10)) };
        }

        // 8 bytes
        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED || dataType == DataType.EIGHT_BYTE_INT_SIGNED) {
            long l = value.longValue();
            return new short[] { (short) (l >> 48), (short) (l >> 32), (short) (l >> 16), (short) l };
        }

        if (dataType == DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED || dataType == DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED) {
            long l = value.longValue();
            return new short[] { (short) l, (short) (l >> 16), (short) (l >> 32), (short) (l >> 48) };
        }

        if (dataType == DataType.EIGHT_BYTE_FLOAT) {
            long l = Double.doubleToLongBits(value.doubleValue());
            return new short[] { (short) (l >> 48), (short) (l >> 32), (short) (l >> 16), (short) l };
        }

        if (dataType == DataType.EIGHT_BYTE_FLOAT_SWAPPED) {
            long l = Double.doubleToLongBits(value.doubleValue());
            return new short[] { (short) l, (short) (l >> 16), (short) (l >> 32), (short) (l >> 48) };
        }

        throw new RuntimeException("Unsupported data type: " + dataType);
    }
}
