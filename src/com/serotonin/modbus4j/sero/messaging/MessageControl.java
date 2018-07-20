package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;

import com.serotonin.modbus4j.sero.io.StreamUtils;
import com.serotonin.modbus4j.sero.log.BaseIOLog;
import com.serotonin.modbus4j.sero.timer.SystemTimeSource;
import com.serotonin.modbus4j.sero.timer.TimeSource;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * In general there are three messaging activities:
 * <ol>
 * <li>Send a message for which no reply is expected, e.g. a broadcast.</li>
 * <li>Send a message and wait for a response with timeout and retries.</li>
 * <li>Listen for unsolicited requests.</li>
 * </ol>
 * 
 * @author Matthew Lohbihler
 */
public class MessageControl implements DataConsumer {
    private static int DEFAULT_RETRIES = 2;
    private static int DEFAULT_TIMEOUT = 500;

    public boolean DEBUG = false;

    private Transport transport;
    private MessageParser messageParser;
    private RequestHandler requestHandler;
    private WaitingRoomKeyFactory waitingRoomKeyFactory;
    private MessagingExceptionHandler exceptionHandler = new DefaultMessagingExceptionHandler();
    private int retries = DEFAULT_RETRIES;
    private int timeout = DEFAULT_TIMEOUT;
    private int discardDataDelay = 0;
    private long lastDataTimestamp;

    private BaseIOLog ioLog;
    private TimeSource timeSource = new SystemTimeSource();

    private final WaitingRoom waitingRoom = new WaitingRoom();
    private final ByteQueue dataBuffer = new ByteQueue();

    public void start(Transport transport, MessageParser messageParser, RequestHandler handler,
            WaitingRoomKeyFactory waitingRoomKeyFactory) throws IOException {
        this.transport = transport;
        this.messageParser = messageParser;
        this.requestHandler = handler;
        this.waitingRoomKeyFactory = waitingRoomKeyFactory;
        waitingRoom.setKeyFactory(waitingRoomKeyFactory);
        transport.setConsumer(this);
    }

    public void close() {
        transport.removeConsumer();
    }

    public void setExceptionHandler(MessagingExceptionHandler exceptionHandler) {
        if (exceptionHandler == null)
            this.exceptionHandler = new DefaultMessagingExceptionHandler();
        else
            this.exceptionHandler = exceptionHandler;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDiscardDataDelay() {
        return discardDataDelay;
    }

    public void setDiscardDataDelay(int discardDataDelay) {
        this.discardDataDelay = discardDataDelay;
    }

    public BaseIOLog getIoLog() {
        return ioLog;
    }

    public void setIoLog(BaseIOLog ioLog) {
        this.ioLog = ioLog;
    }

    public TimeSource getTimeSource() {
        return timeSource;
    }

    public void setTimeSource(TimeSource timeSource) {
        this.timeSource = timeSource;
    }

    public IncomingResponseMessage send(OutgoingRequestMessage request) throws IOException {
        return send(request, timeout, retries);
    }

    public IncomingResponseMessage send(OutgoingRequestMessage request, int timeout, int retries) throws IOException {
        byte[] data = request.getMessageData();
        if (DEBUG)
            System.out.println("MessagingControl.send: " + StreamUtils.dumpHex(data));

        IncomingResponseMessage response = null;

        if (request.expectsResponse()) {
            WaitingRoomKey key = waitingRoomKeyFactory.createWaitingRoomKey(request);

            // Enter the waiting room
            waitingRoom.enter(key);

            try {
                do {
                    // Send the request.
                    write(data);

                    // Wait for the response.
                    response = waitingRoom.getResponse(key, timeout);

                    if (DEBUG && response == null)
                        System.out.println("Timeout waiting for response");
                }
                while (response == null && retries-- > 0);
            }
            finally {
                // Leave the waiting room.
                waitingRoom.leave(key);
            }

            if (response == null)
                throw new TimeoutException("request=" + request);
        }
        else
            write(data);

        return response;
    }

    public void send(OutgoingResponseMessage response) throws IOException {
        write(response.getMessageData());
    }

    /**
     * Incoming data from the transport. Single-threaded.
     */
    public void data(byte[] b, int len) {
        if (DEBUG)
            System.out.println("MessagingConnection.read: " + StreamUtils.dumpHex(b, 0, len));
        if (ioLog != null)
            ioLog.input(b, 0, len);

        if (discardDataDelay > 0) {
            long now = timeSource.currentTimeMillis();
            if (now - lastDataTimestamp > discardDataDelay)
                dataBuffer.clear();
            lastDataTimestamp = now;
        }

        dataBuffer.push(b, 0, len);

        // There may be multiple messages in the data, so enter a loop.
        while (true) {
            // Attempt to parse a message.
            try {
                // Mark where we are in the buffer. The entire message may not be in yet, but since the parser
                // will consume the buffer we need to be able to backtrack.
                dataBuffer.mark();

                IncomingMessage message = messageParser.parseMessage(dataBuffer);

                if (message == null) {
                    // Nothing to do. Reset the buffer and exit the loop.
                    dataBuffer.reset();
                    break;
                }

                if (message instanceof IncomingRequestMessage) {
                    // Received a request. Give it to the request handler
                    if (requestHandler != null) {
                        OutgoingResponseMessage response = requestHandler
                                .handleRequest((IncomingRequestMessage) message);

                        if (response != null)
                            send(response);
                    }
                }
                else
                    // Must be a response. Give it to the waiting room.
                    waitingRoom.response((IncomingResponseMessage) message);
            }
            catch (Exception e) {
                exceptionHandler.receivedException(e);
                // Clear the buffer
                //                dataBuffer.clear();
            }
        }
    }

    private void write(byte[] data) throws IOException {
        if (ioLog != null)
            ioLog.output(data);

        synchronized (transport) {
            transport.write(data);
        }
    }

    public void handleIOException(IOException e) {
        exceptionHandler.receivedException(e);
    }
}
