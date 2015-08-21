/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.modbus4j.sero.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Terry Packer
 *
 */
public class StreamTransportCharSpaced extends StreamTransport{

	private final long charSpacing;
	
	/**
	 * @param in
	 * @param out
	 */
	public StreamTransportCharSpaced(InputStream in, OutputStream out, long charSpacing) {
		super(in, out);
		this.charSpacing = charSpacing;
	}

	/**
	 * Perform a write, ensure space between chars
	 */
	@Override
    public void write(byte[] data) throws IOException {
		
		try{
		long waited = 0,writeStart,writeEnd, waitRemaining;
			for(byte b : data){
				writeStart = System.nanoTime();
				out.write(b);
				writeEnd = System.nanoTime();
				waited = writeEnd - writeStart;
				if(waited < this.charSpacing){
					waitRemaining = this.charSpacing - waited;
					Thread.sleep(waitRemaining / 1000000, (int)(waitRemaining % 1000000));
				}
					
			}
		}catch(Exception e){
			throw new IOException(e);
		}
        out.flush();
    }

    public void write(byte[] data, int len) throws IOException {
		try{
		long waited = 0,writeStart,writeEnd, waitRemaining;
			for(int i=0; i< len; i++){
				writeStart = System.nanoTime();
				out.write(data[i]);
				writeEnd = System.nanoTime();
				waited = writeEnd - writeStart;
				if(waited < this.charSpacing){
					waitRemaining = this.charSpacing - waited;
					Thread.sleep(waitRemaining / 1000000, (int)(waitRemaining % 1000000));
				}
					
			}
		}catch(Exception e){
			throw new IOException(e);
		}
        out.flush();
    }
	
	
}
