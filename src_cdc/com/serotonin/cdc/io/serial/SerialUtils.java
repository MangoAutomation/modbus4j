/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.

    This program is free software; you can redistribute it and/or modify
    it under the terms of version 2 of the GNU General Public License as 
    published by the Free Software Foundation and additional terms as 
    specified by Serotonin Software Technologies Inc.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

 	@author Matthew Lohbihler
 */

package com.serotonin.cdc.io.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.RXTXHack;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;

/**
 * @author Matthew Lohbihler
 * 
 */
public class SerialUtils {
    public static SerialPort openSerialPort(SerialParameters serialParameters) throws SerialPortException {
        SerialPort serialPort = null;
        try {
            // Open the serial port.
            CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(serialParameters.getCommPortId());

            if (cpi.getPortType() != CommPortIdentifier.PORT_SERIAL)
                throw new SerialPortException("Port with id " + serialParameters.getCommPortId()
                        + " is not a serial port");

            serialPort = (SerialPort) cpi.open(serialParameters.getPortOwnerName(), 1000);
            serialPort.setSerialPortParams(serialParameters.getBaudRate(), serialParameters.getDataBits(),
                    serialParameters.getStopBits(), serialParameters.getParity());
            serialPort.setFlowControlMode(serialParameters.getFlowControlIn() | serialParameters.getFlowControlOut());
        }
        catch (SerialPortException e) {
            close(serialPort);
            throw e;
        }
        catch (Exception e) {
            close(serialPort);
            // Wrap all exceptions in the init exception type.
            throw new SerialPortException(e);
        }

        return serialPort;
    }

    public static void close(SerialPort serialPort) {
        if (serialPort != null) {
            if (serialPort instanceof RXTXPort)
                RXTXHack.closeRxtxPort((RXTXPort) serialPort);
            else
                serialPort.close();
        }
    }
}
