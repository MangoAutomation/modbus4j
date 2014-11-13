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
package com.serotonin.modbus4j.serial.rtu;

import java.io.IOException;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.io.serial.SerialParameters;
import com.serotonin.messaging.MessageControl;
import com.serotonin.messaging.StreamTransport;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.serial.SerialMaster;
import com.serotonin.modbus4j.serial.SerialWaitingRoomKeyFactory;

public class RtuMaster extends SerialMaster {
	
	
	
    // Runtime fields.
    private MessageControl conn;
    private long lastSendTime; //Last time sent (Nano-time, not wall clock time)
    private long messageFrameSpacing; //Time in ns

    public RtuMaster(SerialParameters params) {
        super(params);
        
        //For Modbus Serial Spec, Message Framing rates at 19200 Baud are fixed
        if(params.getBaudRate() > 19200){
        	this.messageFrameSpacing = 1750000; //Nanoseconds
        	this.characterSpacing = 750000; //Nanoseconds
        }else{
        
	        //Compute the char size
	        float charBits = params.getDataBits();
	        switch(params.getStopBits()){
	        case 1:
	        	//Strangely this results in 0 stop bits.. in JSSC code
	        	break;
	        case 2:
	        	charBits += 2f;
	        	break;
	        case 3:
	        	//1.5 stop bits
	        	charBits += 1.5f;
	        	break;
	        default:
	        	throw new ShouldNeverHappenException("Unknown stop bit size: " + params.getStopBits());
	        }
	        
	        if(params.getParity() > 0)
	        	charBits += 1; //Add another if using parity
        
	        //Compute ns it takes to send one char
	        // ((charSize/symbols per second) ) * ns per second
	        float charTime = (charBits / (float)params.getBaudRate()) * 1000000000f;
	        this.messageFrameSpacing = (long)(charTime * 3.5f);
	        this.characterSpacing = (long)(charTime * 1.5f);
        }
        
    }

    @Override
    public void init() throws ModbusInitException {
        super.init();

        RtuMessageParser rtuMessageParser = new RtuMessageParser(true);
        conn = getMessageControl();
        try {
            conn.start(transport, rtuMessageParser, null, new SerialWaitingRoomKeyFactory());
            if (getePoll() == null)
                ((StreamTransport) transport).start("Modbus RTU master");
        }
        catch (IOException e) {
            throw new ModbusInitException(e);
        }
        initialized = true;
    }

    @Override
    public void destroy() {
        closeMessageControl(conn);
        super.close();
    }

    @Override
    public ModbusResponse sendImpl(ModbusRequest request) throws ModbusTransportException {
        // Wrap the modbus request in an rtu request.
        RtuMessageRequest rtuRequest = new RtuMessageRequest(request);

        // Send the request to get the response.
        RtuMessageResponse rtuResponse;
        try {
        	//Wait 3.5 char lengths
        	long waited = System.nanoTime() - this.lastSendTime;
        	if(waited < this.messageFrameSpacing){
        			Thread.sleep(this.messageFrameSpacing / 1000000, (int)(this.messageFrameSpacing % 1000000));
        	}
            rtuResponse = (RtuMessageResponse) conn.send(rtuRequest);
            if (rtuResponse == null)
                return null;
            return rtuResponse.getModbusResponse();
        }
        catch (Exception e) {
            throw new ModbusTransportException(e, request.getSlaveId());
        }finally{
        	//Update our last send time
        	this.lastSendTime = System.nanoTime();
        }
    }
}
