package com.serotonin.cdc.io;

public class StreamUtils {
    public static String dumpMessage(byte[] b) {
        return dumpMessage(b, 0, b.length);
    }

    public static String dumpMessage(byte[] b, int pos, int len) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = pos; i < len; i++) {
            switch (b[i]) {
            case 2:
                sb.append("&STX;");
                break;
            case 3:
                sb.append("&ETX;");
                break;
            case 27:
                sb.append("&ESC;");
                break;
            default:
                sb.append((char) b[i]);
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
