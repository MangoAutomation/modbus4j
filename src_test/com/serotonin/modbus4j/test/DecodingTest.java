package com.serotonin.modbus4j.test;

import com.serotonin.modbus4j.serial.rtu.RtuMessageParser;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class DecodingTest {
    public static void main(String[] args) throws Exception {
        //        decodeRequest("0A0300770001356B");
        //        decodeResponse("0A030200001D85");
        //        decodeRequest("0A0600770001F96B");

        //        decodeRequest("011000a80002043535353530f4");
        //        decodeResponse("011000a80002042993");
        decodeRequest("010300010001D5CA");
    }

    public static void decodeRequest(String s) throws Exception {
        ByteQueue queue = new ByteQueue(toBytes(s));
        new RtuMessageParser(false).parseMessage(queue);
    }

    public static void decodeResponse(String s) throws Exception {
        ByteQueue queue = new ByteQueue(toBytes(s));
        new RtuMessageParser(true).parseMessage(queue);
    }

    public static byte[] toBytes(String s) {
        if (s.startsWith("["))
            s = s.substring(1);
        if (s.endsWith("]"))
            s = s.substring(0, s.length() - 1);
        String[] parts = s.split(",");
        if (parts.length == 1)
            parts = s.split("\\|");
        if (parts.length == 1)
            parts = s.split(" ");
        if (parts.length == 1) {
            parts = new String[s.length() / 2];
            for (int i = 0; i < parts.length; i++)
                parts[i] = s.substring(i * 2, i * 2 + 2);
        }

        byte[] bytes = new byte[parts.length];

        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) Integer.parseInt(parts[i].trim(), 16);

        return bytes;
    }
}
