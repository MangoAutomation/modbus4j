/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.modbus4j.test;

import java.util.Random;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.ProcessImageListener;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.IllegalDataAddressException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

/**
 * Test the data types:
 *     public static final int TWO_BYTE_INT_UNSIGNED_SWAPPED = 22;
 *     public static final int TWO_BYTE_INT_SIGNED_SWAPPED = 23;
 *     public static final int FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED = 24;
 *     public static final int FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED = 25;
 * 
 * @author Terry Packer
 */
public class Test4 {
    static Random random = new Random();
    static float ir1Value = -100;
    
    //TEST VALUES
    private static Integer twoByteIntUnsignedSwapped = new Integer(29187); //Register 16
    private static Integer twoByteIntSignedSwapped = new Integer(-257); //Register 17
    private static Long fourByteIntUnsignedSwappedSwapped = new Long(16777216); //Register 18
    private static Long fourByteIntSignedSwappedSwapped = new Long(-16777217); //Register 20
    private static Long register22 = new Long(2369850368L); ////Register 22

    public static void main(String[] args) throws Exception {
        // SerialParameters params = new SerialParameters();
        // params.setCommPortId("COM1");
        // params.setPortOwnerName("dufus");
        // params.setBaudRate(9600);

        // IpParameters params = new IpParameters();
        // params.setHost(host)


        // ModbusListener listener = modbusFactory.createRtuListener(processImage, 31, params, false);
        // ModbusListener listener = modbusFactory.createAsciiListener(processImage, 31, params);
        int port = 5000;
        final ModbusSlaveSet listener = new TcpSlave(port, false);
        // ModbusSlave listener = modbusFactory.createUdpSlave(processImage, 31);

        // Add a few slave process images to the listener.
        listener.addProcessImage(getModscanProcessImage(1));
        //        listener.addProcessImage(getModscanProcessImage(2));
        //listener.addProcessImage(getModscanProcessImage(3));
        //        listener.addProcessImage(getModscanProcessImage(5));
        //listener.addProcessImage(getModscanProcessImage(9));

        // When the "listener" is started it will use the current thread to run. So, if an exception is not thrown
        // (and we hope it won't be), the method call will not return. Therefore, we start the listener in a separate
        // thread so that we can use this thread to modify the values.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.start();
                }
                catch (ModbusInitException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            synchronized (listener) {
                listener.wait(200);
            }

            for (ProcessImage processImage : listener.getProcessImages())
                updateProcessImage((BasicProcessImage) processImage);
        }
    }

    static void updateProcessImage(BasicProcessImage processImage) throws IllegalDataAddressException {

        int hr16Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED_SWAPPED).intValue();
        if(hr16Value != twoByteIntUnsignedSwapped){
        	throw new RuntimeException("Test failed on TWO_BYTE_INT_UNSIGNED_SWAPPED. Expected " + twoByteIntUnsignedSwapped + " but was: " + hr16Value);
        }
        
        short hr17Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 17, DataType.TWO_BYTE_INT_SIGNED_SWAPPED).shortValue();
        if(hr17Value != twoByteIntSignedSwapped){
        	throw new RuntimeException("Test failed on TWO_BYTE_INT_SIGNED_SWAPPED. Expected " + twoByteIntSignedSwapped + " but was: " + hr17Value);
        }
        
        long hr18Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 18, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED).longValue();
        if(hr18Value != fourByteIntUnsignedSwappedSwapped){
        	throw new RuntimeException("Test failed on FOUR_BYTE_INT_UNSIGNED_SWAPPED_INVERTED. Expected " + fourByteIntUnsignedSwappedSwapped + "  but was: " + hr18Value);
        }
        
        int hr20Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 20, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED).intValue();
        if(hr20Value != fourByteIntSignedSwappedSwapped){
        	throw new RuntimeException("Test failed on FOUR_BYTE_INT_SIGNED_SWAPPED_INVERTED. Expected  " +  fourByteIntSignedSwappedSwapped + "  but was: " + hr20Value);
        }

        long hr22Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 22, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED).longValue();
        if(hr22Value != register22){
        	throw new RuntimeException("Test failed on FOUR_BYTE_INT_UNSIGNED_SWAPPED_INVERTED. Expected " + register22 + " but was: " + hr22Value);
        }
        
    }

    static class BasicProcessImageListener implements ProcessImageListener {
        @Override
        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
            System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
        }

        @Override
        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
            // Add a small delay to the processing.
            //            try {
            //                Thread.sleep(500);
            //            }
            //            catch (InterruptedException e) {
            //                // no op
            //            }
            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
        }
    }

    static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        //processImage.setAllowInvalidAddress(true);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);
        
        //Register 16 Holds 1 and the data is transmitted as 0b00000001 00000000 which is 256
        //processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED_SWAPPED, new Integer(256));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED_SWAPPED, new Integer(29187));
        //Registery 16 Holds -2 and the data is transmitted as 0b1111110 11111111 which is -257
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 17, DataType.TWO_BYTE_INT_SIGNED_SWAPPED, new Integer(-257));

        //Register 18 Holds 1 and the data is transmitted as 0b00000001 00000000 00000000 00000000 which is 16777216
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 18, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED, new Long(16777216));
        
        //Register 20 Holds -2 and the data is transmitted as 0b1111110 11111111 1111111 11111111 which is -16777217
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 20, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED_SWAPPED, new Long(-16777217));

        //Register 22 Holds 803213 and the data is transmitted as 0b10001101 01000001 00001100 00000000 which is 2369850368
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 22, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED_SWAPPED, new Long(2369850368L));
        
        
        processImage.setExceptionStatus((byte) 151);

        // Add an image listener.
        processImage.addListener(new BasicProcessImageListener());

        return processImage;
    }
}

