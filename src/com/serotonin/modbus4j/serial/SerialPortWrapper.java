/**
 * Copyright (C) 2015 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.modbus4j.serial;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wrapper to further aid in abstracting Modbus4J from a serial port implementation
 *
 * @author Terry Packer
 * @version 5.0.0
 */
public interface SerialPortWrapper {

	/**
	 * Close the Serial Port
	 *
	 * @throws java.lang.Exception if any.
	 */
	void close() throws Exception;

	/**
	 * <p>open.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	void open() throws Exception;

	/**
	 *
	 * Return the input stream for an open port
	 *
	 * @return a {@link java.io.InputStream} object.
	 */
	InputStream getInputStream();

	/**
	 * Return the output stream for an open port
	 *
	 * @return a {@link java.io.OutputStream} object.
	 */
	OutputStream getOutputStream();

	/**
	 * <p>getBaudRate.</p>
	 *
	 * @return a int.
	 */
	int getBaudRate();
	
	/**
	 * <p>getDataBits.</p>
	 *
	 * @return a int.
	 */
	int getDataBits();

	/**
	 * <p>getStopBits.</p>
	 *
	 * @return a int.
	 */
	int getStopBits();

	/**
	 * <p>getParity.</p>
	 *
	 * @return a int.
	 */
	int getParity();
	
	

}
