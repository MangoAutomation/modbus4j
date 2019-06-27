/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.modbus4j.base;

import com.serotonin.modbus4j.sero.messaging.IncomingMessage;
import com.serotonin.modbus4j.sero.messaging.MessageParser;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * <p>Abstract BaseMessageParser class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
abstract public class BaseMessageParser implements MessageParser {
    protected final boolean master;

    /**
     * <p>Constructor for BaseMessageParser.</p>
     *
     * @param master a boolean.
     */
    public BaseMessageParser(boolean master) {
        this.master = master;
    }

    /** {@inheritDoc} */
    @Override
    public IncomingMessage parseMessage(ByteQueue queue) throws Exception {
        try {
            return parseMessageImpl(queue);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // Means that we ran out of data trying to read the message. Just return null.
            return null;
        }
    }

    /**
     * <p>parseMessageImpl.</p>
     *
     * @param queue a {@link com.serotonin.modbus4j.sero.util.queue.ByteQueue} object.
     * @return a {@link com.serotonin.modbus4j.sero.messaging.IncomingMessage} object.
     * @throws java.lang.Exception if any.
     */
    abstract protected IncomingMessage parseMessageImpl(ByteQueue queue) throws Exception;
}
