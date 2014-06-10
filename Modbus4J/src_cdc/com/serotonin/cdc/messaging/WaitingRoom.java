package com.serotonin.cdc.messaging;

import java.util.HashMap;
import java.util.Map;

/**
 * The waiting room is a place for request messages to hang out while awaiting their responses.
 * 
 * @author Matthew Lohbihler
 */
class WaitingRoom {
    private final Map/* <WaitingRoomKey, Member> */waitHere = new HashMap();

    private WaitingRoomKeyFactory keyFactory;

    void setKeyFactory(WaitingRoomKeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    /**
     * The request message should be sent AFTER entering the waiting room so that the (vanishingly small) chance of a
     * response being returned before the thread is waiting for it is eliminated.
     * 
     * @return
     */
    void enter(WaitingRoomKey key) {
        Member member = new Member();
        synchronized (this) {
            while (waitHere.get(key) != null) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    // no op
                }
            }
            //            Member dup = waitHere.get(key);
            //            if (dup != null) {
            //                
            //                throw new WaitingRoomException("Waiting room too crowded. Already contains the key " + key);
            //            }

            waitHere.put(key, member);
        }
    }

    IncomingResponseMessage getResponse(WaitingRoomKey key, long timeout) throws WaitingRoomException {
        // Get the member.
        Member member;
        synchronized (this) {
            member = (Member) waitHere.get(key);
        }

        if (member == null)
            throw new WaitingRoomException("No member for key " + key);

        // Wait for the response.
        return member.getResponse(timeout);
    }

    void leave(WaitingRoomKey key) {
        // Leave the waiting room
        synchronized (this) {
            waitHere.remove(key);

            // Notify any threads that are waiting to get in. This could probably be just a notify() call.
            notifyAll();
        }
    }

    /**
     * This method is used by the data listening thread to post responses as they are received from the transport.
     * 
     * @param response
     *            the response message
     * @throws WaitingRoomException
     */
    void response(IncomingResponseMessage response) throws WaitingRoomException {
        WaitingRoomKey key = keyFactory.createWaitingRoomKey(response);
        Member member;

        synchronized (this) {
            member = (Member) waitHere.get(key);
        }

        if (member != null)
            member.setResponse(response);
        else
            throw new WaitingRoomException("No recipient was found waiting for response for key " + key);
    }

    /**
     * This class is used by network message controllers to manage the blocking of threads sending confirmed messages.
     * The instance itself serves as a monitor upon which the sending thread can wait (with a timeout). When a response
     * is received, the message controller can set it in here, automatically notifying the sending thread that the
     * response is available.
     * 
     * @author Matthew Lohbihler
     */
    class Member {
        private IncomingResponseMessage response;

        synchronized void setResponse(IncomingResponseMessage response) {
            this.response = response;
            notify();
        }

        synchronized IncomingResponseMessage getResponse(long timeout) {
            // Check if there is a response object now.
            if (response != null)
                return response;

            // If not, wait the timeout and then check again.
            waitNoThrow(timeout);
            return response;
        }

        private void waitNoThrow(long timeout) {
            try {
                wait(timeout);
            }
            catch (InterruptedException e) {
                // Ignore
            }
        }
    }
}
