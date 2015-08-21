/**
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.modbus4j.test;

import java.io.InputStream;
import java.io.OutputStream;

import com.serotonin.modbus4j.serial.SerialPortWrapper;

/**
 * 
 * This class is not finished
 * 
 * @author Terry Packer
 *
 */
public class TestSerialPortWrapper implements SerialPortWrapper{
	
	private String commPortId;
    private int baudRate;
    private int flowControlIn;
    private int flowControlOut;
    private int dataBits;
    private int stopBits;
    private int parity;
	
	public TestSerialPortWrapper(String commPortId, int baudRate, int flowControlIn,
			int flowControlOut, int dataBits, int stopBits, int parity){
		
        this.baudRate = baudRate;
        this.flowControlIn = flowControlIn;
        this.flowControlOut = flowControlOut;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
	}
	

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#open()
	 */
	@Override
	public void open() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getBaudRate()
	 */
	@Override
	public int getBaudRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getStopBits()
	 */
	@Override
	public int getStopBits() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getParity()
	 */
	@Override
	public int getParity() {
		// TODO Auto-generated method stub
		return 0;
	}


	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getFlowControlIn()
	 */
	@Override
	public int getFlowControlIn() {
		// TODO Auto-generated method stub
		return 0;
	}


	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getFlowControlOut()
	 */
	@Override
	public int getFlowControlOut() {
		// TODO Auto-generated method stub
		return 0;
	}


	/* (non-Javadoc)
	 * @see com.serotonin.modbus4j.serial.SerialPortWrapper#getDataBits()
	 */
	@Override
	public int getDataBits() {
		// TODO Auto-generated method stub
		return 0;
	}

}
