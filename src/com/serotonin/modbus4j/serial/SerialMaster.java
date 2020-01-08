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
package com.serotonin.modbus4j.serial;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.sero.messaging.EpollStreamTransport;
import com.serotonin.modbus4j.sero.messaging.MessageControl;
import com.serotonin.modbus4j.sero.messaging.StreamTransport;
import com.serotonin.modbus4j.sero.messaging.Transport;

/**
 * <p>Abstract SerialMaster class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
abstract public class SerialMaster extends ModbusMaster {

    private static final int RETRY_PAUSE_START = 50;
    private static final int RETRY_PAUSE_MAX = 1000;

    private final Log LOG = LogFactory.getLog(SerialMaster.class);

    // Runtime fields.
    protected boolean serialPortOpen;
    protected SerialPortWrapper wrapper;
    protected Transport transport;



    /**
     * <p>Constructor for SerialMaster.</p>
     *
     * Default to validating the slave id in responses
     *
     * @param wrapper a {@link com.serotonin.modbus4j.serial.SerialPortWrapper} object.
     */
    public SerialMaster(SerialPortWrapper wrapper) {
        this(wrapper, true);
    }

    /**
     * <p>Constructor for SerialMaster.</p>
     * @param wrapper a {@link com.serotonin.modbus4j.serial.SerialPortWrapper} object.
     * @param validateResponse - confirm that requested slave id is the same in the response
     */
    public SerialMaster(SerialPortWrapper wrapper, boolean validateResponse) {
        this.wrapper = wrapper;
        this.validateResponse = validateResponse;
    }

    /** {@inheritDoc} */
    @Override
    public void init() throws ModbusInitException {
        try {
            this.openConnection(null);
        }
        catch (Exception e) {
            throw new ModbusInitException(e);
        }
    }

    /**
     * Open the serial port and initialize the transport, ensure
     * connection is closed first
     *
     * @param conn
     * @throws Exception
     */
    protected void openConnection(MessageControl toClose) throws Exception {
        // Make sure any existing connection is closed.
        closeConnection(toClose);

        // Try 'retries' times to get the socket open.
        int retries = getRetries();
        int retryPause = RETRY_PAUSE_START;
        while (true) {
            try {
                this.wrapper.open();
                this.serialPortOpen = true;
                if (getePoll() != null) {
                    transport = new EpollStreamTransport(wrapper.getInputStream(),
                            wrapper.getOutputStream(),
                            getePoll());
                }else {
                    transport = new StreamTransport(wrapper.getInputStream(),
                            wrapper.getOutputStream());
                }
                break;
            }catch(Exception e) {
                //Ensure port is closed before we try to reopen or bail out
                close();

                if (retries <= 0)
                    throw e;

                retries--;

                // Pause for a bit.
                try {
                    Thread.sleep(retryPause);
                }
                catch (InterruptedException e1) {
                    // ignore
                }
                retryPause *= 2;
                if (retryPause > RETRY_PAUSE_MAX)
                    retryPause = RETRY_PAUSE_MAX;
            }
        }
    }

    /**
     * Close serial port
     * @param conn
     */
    protected void closeConnection(MessageControl conn) {
        closeMessageControl(conn);
        try {
            if(serialPortOpen) {
                wrapper.close();
                serialPortOpen = false;
            }
        }
        catch (Exception e) {
            getExceptionHandler().receivedException(e);
        }

        transport = null;
    }

    /**
     * <p>close.</p>
     */
    public void close() {
        try {
            wrapper.close();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
