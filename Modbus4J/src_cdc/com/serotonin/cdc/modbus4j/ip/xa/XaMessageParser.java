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
package com.serotonin.cdc.modbus4j.ip.xa;

import com.serotonin.cdc.messaging.IncomingMessage;
import com.serotonin.cdc.modbus4j.base.BaseMessageParser;
import com.serotonin.cdc.util.queue.ByteQueue;

public class XaMessageParser extends BaseMessageParser {
    public XaMessageParser(boolean master) {
        super(master);
    }

    //Override
    protected IncomingMessage parseMessageImpl(ByteQueue queue) throws Exception {
        if (master)
            return XaMessageResponse.createXaMessageResponse(queue);
        return XaMessageRequest.createXaMessageRequest(queue);
    }
}
