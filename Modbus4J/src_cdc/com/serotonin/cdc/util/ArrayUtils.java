package com.serotonin.cdc.util;

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

    public static boolean contains(int[] values, int value) {
        if (values == null)
            return false;

        for (int i = 0; i < values.length; i++) {
            if (values[i] == value)
                return true;
        }

        return false;
    }
}
