package com.serotonin.modbus4j.logs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.serotonin.modbus4j.base.BaseMessageParser;
import com.serotonin.modbus4j.serial.rtu.RtuMessageParser;
import com.serotonin.modbus4j.sero.io.LineHandler;
import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.messaging.IncomingMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class LogReader {
    //    static String filename = "logs/anders.txt";
    //    static XaMessageParser inputParser = new XaMessageParser(true);
    //    static XaMessageParser outputParser = new XaMessageParser(false);

    //    static String filename = "logs/iainShort.txt";
    //    static RtuMessageParser inputParser = new RtuMessageParser(true);
    //    static RtuMessageParser outputParser = new RtuMessageParser(false);

    static String filename = "logs/pico.txt";
    static RtuMessageParser inputParser = new RtuMessageParser(true);
    static RtuMessageParser outputParser = new RtuMessageParser(false);

    static boolean diffTS = false;
    static SimpleDateFormat insdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss,SSS");
    static SimpleDateFormat outsdf = new SimpleDateFormat("MM/dd HH:mm:ss.SSS");

    static ByteQueue inputQueue = new ByteQueue();
    static ByteQueue outputQueue = new ByteQueue();

    public static void main(String[] args) throws IOException {
        StreamUtils.readLines(filename, new LineHandler() {
            int lineNum = 0;
            long lastTime = 0;

            @Override
            public void handleLine(String line) {
                try {
                    lineNum++;

                    String[] parts = line.split("\\s");

                    long ts = insdf.parse(parts[0]).getTime();
                    logTime(lastTime, ts);

                    if ("I".equals(parts[1])) {
                        // input
                        System.out.println("IN " + parts[2]);
                        inputQueue.push(parts[2]);
                        parseData(inputQueue, inputParser);
                    }
                    else if ("O".equals(parts[1])) {
                        // output
                        System.out.println("OUT " + parts[2]);
                        outputQueue.push(parts[2]);
                        parseData(outputQueue, outputParser);
                    }
                    else {
                        // comment
                        String comment = line.substring(line.indexOf(" ") + 1);
                        System.out.println(comment);
                    }

                    lastTime = ts;
                }
                catch (Exception e) {
                    throw new RuntimeException("Exception on line " + lineNum, e);
                }

                //                if (lineNum > 5000)
                //                    throw new RuntimeException("STOP");
            }

            @Override
            public void done() {
                // no op
            }
        });
    }

    static void parseData(ByteQueue queue, BaseMessageParser parser) throws Exception {
        IncomingMessage m;
        while (true) {
            queue.mark();
            try {
                m = parser.parseMessage(queue);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                continue;
            }
            if (m == null) {
                queue.reset();
                return;
            }
            System.out.println("      " + m);
        }
    }

    static void logTime(long lastTime, long time) {
        if (diffTS) {
            if (lastTime == 0)
                lastTime = time;
            long diff = time - lastTime;
            System.out.print(Long.toString(diff) + " ");
        }
        else
            System.out.print(outsdf.format(new Date(time)) + " ");
    }
}
