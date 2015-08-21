package com.serotonin.modbus4j.sero.util;

import java.util.List;

public class ArrayUtils {
    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, 0, bytes.length);
    }

    public static String toHexString(byte[] bytes, int start, int len) {
        if (len == 0)
            return "[]";

        StringBuffer sb = new StringBuffer();
        sb.append('[');
        sb.append(Integer.toHexString(bytes[start] & 0xff));
        for (int i = 1; i < len; i++)
            sb.append(',').append(Integer.toHexString(bytes[start + i] & 0xff));
        sb.append("]");

        return sb.toString();
    }

    public static String toPlainHexString(byte[] bytes) {
        return toPlainHexString(bytes, 0, bytes.length);
    }

    public static String toPlainHexString(byte[] bytes, int start, int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String s = Integer.toHexString(bytes[start + i] & 0xff);
            if (s.length() < 2)
                sb.append('0');
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toString(byte[] bytes) {
        return toString(bytes, 0, bytes.length);
    }

    public static String toString(byte[] bytes, int start, int len) {
        if (len == 0)
            return "[]";

        StringBuffer sb = new StringBuffer();
        sb.append('[');
        sb.append(Integer.toString(bytes[start] & 0xff));
        for (int i = 1; i < len; i++)
            sb.append(',').append(Integer.toString(bytes[start + i] & 0xff));
        sb.append("]");

        return sb.toString();
    }

    public static boolean isEmpty(int[] value) {
        return value == null || value.length == 0;
    }

    public static int indexOf(String[] values, String value) {
        if (values == null)
            return -1;

        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value))
                return i;
        }

        return -1;
    }

    public static boolean containsIgnoreCase(String[] values, String value) {
        if (values == null)
            return false;

        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(value))
                return true;
        }

        return false;
    }

    public static int indexOf(byte[] src, byte[] target) {
        return indexOf(src, 0, src.length, target);
    }

    public static int indexOf(byte[] src, int len, byte[] target) {
        return indexOf(src, 0, len, target);
    }

    public static int indexOf(byte[] src, int start, int len, byte[] target) {
        int pos = start;
        int i;
        boolean matched;
        while (pos + target.length <= len) {
            // Check for a match on the first character
            if (src[pos] == target[0]) {
                // Now check for matches in the rest of the characters
                matched = true;
                i = 1;
                while (i < target.length) {
                    if (src[pos + i] != target[i]) {
                        matched = false;
                        break;
                    }
                    i++;
                }

                if (matched)
                    return pos;
            }
            pos++;
        }

        return -1;
    }

    /**
     * Returns the value of the bits in the given range. Ranges can extend multiple bytes. No range checking is done.
     * Invalid ranges will result in {@link ArrayIndexOutOfBoundsException}.
     * 
     * @param b
     *            the array of bytes.
     * @param offset
     *            the location at which to begin
     * @param length
     *            the number of bits to include in the value.
     * @return the value of the bits in the range.
     */
    public static long bitRangeValueLong(byte[] b, int offset, int length) {
        if (length <= 0)
            return 0;

        int byteFrom = offset / 8;
        int byteTo = (offset + length - 1) / 8;

        long result = b[byteFrom] & bitFromMask[offset % 8];

        for (int i = byteFrom + 1; i <= byteTo; i++) {
            result <<= 8;
            result |= b[i] & 0xff;
        }

        result >>= 8 - (((offset + length - 1) % 8) + 1);

        return result;
    }

    private static int[] bitFromMask = { 0xff, 0x7f, 0x3f, 0x1f, 0xf, 0x7, 0x3, 0x1 };

    public static int bitRangeValue(byte[] b, int offset, int length) {
        return (int) bitRangeValueLong(b, offset, length);
    }

    public static long byteRangeValueLong(byte[] b, int offset, int length) {
        long result = 0;

        for (int i = offset; i < offset + length; i++) {
            result <<= 8;
            result |= b[i] & 0xff;
        }

        return result;
    }

    public static int byteRangeValue(byte[] b, int offset, int length) {
        return (int) byteRangeValueLong(b, offset, length);
    }

    public static int sum(int[] a) {
        int sum = 0;
        for (int i = 0; i < a.length; i++)
            sum += a[i];
        return sum;
    }

    public static int[] toIntArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = list.get(i);
        return result;
    }

    public static double[] toDoubleArray(List<Double> list) {
        double[] result = new double[list.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = list.get(i);
        return result;
    }

    public static String concatenate(Object[] a, String delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object o : a) {
            if (first)
                first = false;
            else
                sb.append(delimiter);
            sb.append(o);
        }
        return sb.toString();
    }

    public static void shift(Object[] a, int count) {
        if (count > 0)
            System.arraycopy(a, 0, a, count, a.length - count);
        else
            System.arraycopy(a, -count, a, 0, a.length + count);
    }
}
