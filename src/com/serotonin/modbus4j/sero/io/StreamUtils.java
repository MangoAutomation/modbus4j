/*
 * Created on 1-Mar-2006
 */
package com.serotonin.modbus4j.sero.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StreamUtils {
    public static void transfer(InputStream in, OutputStream out) throws IOException {
        transfer(in, out, -1);
    }

    public static void transfer(InputStream in, OutputStream out, long limit) throws IOException {
        byte[] buf = new byte[1024];
        int readcount;
        long total = 0;
        while ((readcount = in.read(buf)) != -1) {
            if (limit != -1) {
                if (total + readcount > limit)
                    readcount = (int) (limit - total);
            }

            if (readcount > 0)
                out.write(buf, 0, readcount);

            total += readcount;
            if (limit != -1 && total >= limit)
                break;
        }
        out.flush();
    }

    public static void transfer(InputStream in, SocketChannel out) throws IOException {
        byte[] buf = new byte[1024];
        ByteBuffer bbuf = ByteBuffer.allocate(1024);
        int len;
        while ((len = in.read(buf)) != -1) {
            bbuf.put(buf, 0, len);
            bbuf.flip();
            while (bbuf.remaining() > 0)
                out.write(bbuf);
            bbuf.clear();
        }
    }

    public static void transfer(Reader reader, Writer writer) throws IOException {
        transfer(reader, writer, -1);
    }

    public static void transfer(Reader reader, Writer writer, long limit) throws IOException {
        char[] buf = new char[1024];
        int readcount;
        long total = 0;
        while ((readcount = reader.read(buf)) != -1) {
            if (limit != -1) {
                if (total + readcount > limit)
                    readcount = (int) (limit - total);
            }

            if (readcount > 0)
                writer.write(buf, 0, readcount);

            total += readcount;
            if (limit != -1 && total >= limit)
                break;
        }
        writer.flush();
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
        transfer(in, out);
        return out.toByteArray();
    }

    public static char[] read(Reader reader) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        transfer(reader, writer);
        return writer.toCharArray();
    }

    public static char readChar(InputStream in) throws IOException {
        return (char) in.read();
    }

    public static String readString(InputStream in, int length) throws IOException {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(readChar(in));
        return sb.toString();
    }

    public static byte readByte(InputStream in) throws IOException {
        return (byte) in.read();
    }

    public static int read4ByteSigned(InputStream in) throws IOException {
        return in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
    }

    public static long read4ByteUnsigned(InputStream in) throws IOException {
        return in.read() | (in.read() << 8) | (in.read() << 16) | (in.read() << 24);
    }

    public static int read2ByteUnsigned(InputStream in) throws IOException {
        return in.read() | (in.read() << 8);
    }

    public static short read2ByteSigned(InputStream in) throws IOException {
        return (short) (in.read() | (in.read() << 8));
    }

    public static void writeByte(OutputStream out, byte b) throws IOException {
        out.write(b);
    }

    public static void writeChar(OutputStream out, char c) throws IOException {
        out.write((byte) c);
    }

    public static void writeString(OutputStream out, String s) throws IOException {
        for (int i = 0; i < s.length(); i++)
            writeChar(out, s.charAt(i));
    }

    public static void write4ByteSigned(OutputStream out, int i) throws IOException {
        out.write((byte) (i & 0xFF));
        out.write((byte) ((i >> 8) & 0xFF));
        out.write((byte) ((i >> 16) & 0xFF));
        out.write((byte) ((i >> 24) & 0xFF));
    }

    public static void write4ByteUnsigned(OutputStream out, long l) throws IOException {
        out.write((byte) (l & 0xFF));
        out.write((byte) ((l >> 8) & 0xFF));
        out.write((byte) ((l >> 16) & 0xFF));
        out.write((byte) ((l >> 24) & 0xFF));
    }

    public static void write2ByteUnsigned(OutputStream out, int i) throws IOException {
        out.write((byte) (i & 0xFF));
        out.write((byte) ((i >> 8) & 0xFF));
    }

    public static void write2ByteSigned(OutputStream out, short s) throws IOException {
        out.write((byte) (s & 0xFF));
        out.write((byte) ((s >> 8) & 0xFF));
    }

    public static String dumpArray(byte[] b) {
        return dumpArray(b, 0, b.length);
    }

    public static String dumpArray(byte[] b, int pos, int len) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = pos; i < len; i++) {
            if (i > 0)
                sb.append(",");
            sb.append(b[i]);
        }
        sb.append(']');
        return sb.toString();
    }

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

    public static String dumpArrayHex(byte[] b) {
        return dumpArrayHex(b, 0, b.length);
    }

    public static String dumpArrayHex(byte[] b, int pos, int len) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = pos; i < len; i++) {
            if (i > 0)
                sb.append(",");
            sb.append(Integer.toHexString(b[i] & 0xff));
        }
        sb.append(']');
        return sb.toString();
    }

    public static String dumpHex(byte[] b) {
        return dumpHex(b, 0, b.length);
    }

    public static String dumpHex(byte[] b, int pos, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = pos; i < len; i++)
            sb.append(StringUtils.leftPad(Integer.toHexString(b[i] & 0xff), 2, '0'));
        return sb.toString();
    }

    public static String readFile(String filename) throws IOException {
        return readFile(new File(filename));
    }

    public static String readFile(File file) throws IOException {
        FileReader in = null;
        try {
            in = new FileReader(file);
            StringWriter out = new StringWriter();
            transfer(in, out);
            return out.toString();
        }
        finally {
            if (in != null)
                in.close();
        }
    }

    public static List<String> readLines(String filename) throws IOException {
        return readLines(new File(filename));
    }

    public static List<String> readLines(File file) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null)
                lines.add(line);
            return lines;
        }
        finally {
            if (in != null)
                in.close();
        }
    }

    public static void writeFile(String filename, String content) throws IOException {
        writeFile(new File(filename), content);
    }

    public static void writeFile(File file, String content) throws IOException {
        FileWriter out = null;
        try {
            out = new FileWriter(file);
            out.write(content);
        }
        finally {
            if (out != null)
                out.close();
        }
    }

    public static void readLines(String filename, LineHandler lineHandler) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = in.readLine()) != null)
                lineHandler.handleLine(line);
            lineHandler.done();
        }
        finally {
            if (in != null)
                in.close();
        }
    }

    public static String toHex(byte[] bs) {
        StringBuilder sb = new StringBuilder(bs.length * 2);
        for (byte b : bs)
            sb.append(StringUtils.leftPad(Integer.toHexString(b & 0xff), 2, '0'));
        return sb.toString();
    }

    public static String toHex(byte b) {
        return StringUtils.leftPad(Integer.toHexString(b & 0xff), 2, '0');
    }

    public static String toHex(short s) {
        return StringUtils.leftPad(Integer.toHexString(s & 0xffff), 4, '0');
    }

    public static String toHex(int i) {
        return StringUtils.leftPad(Integer.toHexString(i), 8, '0');
    }

    public static String toHex(long l) {
        return StringUtils.leftPad(Long.toHexString(l), 16, '0');
    }

    public static byte[] fromHex(String s) {
        byte[] bs = new byte[s.length() / 2];
        for (int i = 0; i < bs.length; i++)
            bs[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        return bs;
    }
}
