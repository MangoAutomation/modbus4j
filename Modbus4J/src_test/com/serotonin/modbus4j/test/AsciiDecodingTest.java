package com.serotonin.modbus4j.test;

import com.serotonin.modbus4j.serial.ascii.AsciiMessageParser;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class AsciiDecodingTest {
    public static void main(String[] args) throws Exception {
        decodeRequest(":010100000002FC\r\n");
        decodeResponse(":01010101FC\r\n");
        decodeRequest(":010300000008F4\r\n");
        decodeResponse(":010310009100000000000000000000000000005B\r\n");
    }

    public static void decodeRequest(String s) throws Exception {
        ByteQueue queue = new ByteQueue(toBytes(s));
        new AsciiMessageParser(false).parseMessage(queue);
    }

    public static void decodeResponse(String s) throws Exception {
        ByteQueue queue = new ByteQueue(toBytes(s));
        new AsciiMessageParser(true).parseMessage(queue);
    }

    public static byte[] toBytes(String s) throws Exception {
        return s.getBytes("ASCII");
    }
}
