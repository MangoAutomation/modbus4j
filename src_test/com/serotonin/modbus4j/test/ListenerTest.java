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

public class ListenerTest {
    static Random random = new Random();
    static float ir1Value = -100;

    public static void main(String[] args) throws Exception {
        // SerialParameters params = new SerialParameters();
        // params.setCommPortId("COM1");
        // params.setPortOwnerName("dufus");
        // params.setBaudRate(9600);

        // IpParameters params = new IpParameters();
        // params.setHost(host)

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusListener listener = modbusFactory.createRtuListener(processImage, 31, params, false);
        // ModbusListener listener = modbusFactory.createAsciiListener(processImage, 31, params);
        final ModbusSlaveSet listener = modbusFactory.createTcpSlave(false);
        // ModbusSlave listener = modbusFactory.createUdpSlave(processImage, 31);

        // Add a few slave process images to the listener.
        listener.addProcessImage(getModscanProcessImage(1));
        //        listener.addProcessImage(getModscanProcessImage(2));
        listener.addProcessImage(getModscanProcessImage(3));
        //        listener.addProcessImage(getModscanProcessImage(5));
        listener.addProcessImage(getModscanProcessImage(9));

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
        processImage.setInput(10, !processImage.getInput(10));
        processImage.setInput(13, !processImage.getInput(13));

        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 20, DataType.FOUR_BYTE_FLOAT, ir1Value += 0.01);

        short hr1Value = processImage.getNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD)
                .shortValue();
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, hr1Value + 1);
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

        processImage.setCoil(10, true);
        processImage.setCoil(11, false);
        processImage.setCoil(12, true);
        processImage.setCoil(13, true);
        processImage.setCoil(14, false);

        processImage.setInput(10, false);
        processImage.setInput(11, false);
        processImage.setInput(12, true);
        processImage.setInput(13, false);
        processImage.setInput(14, true);

        processImage.setBinary(16, true);
        processImage.setBinary(10016, true);

        processImage.setHoldingRegister(10, (short) 1);
        processImage.setHoldingRegister(11, (short) 10);
        processImage.setHoldingRegister(12, (short) 100);
        processImage.setHoldingRegister(13, (short) 1000);
        processImage.setHoldingRegister(14, (short) 10000);

        processImage.setInputRegister(10, (short) 10000);
        processImage.setInputRegister(11, (short) 1000);
        processImage.setInputRegister(12, (short) 100);
        processImage.setInputRegister(13, (short) 10);
        processImage.setInputRegister(14, (short) 1);

        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 3, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 14, true);

        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 15, true);

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_SIGNED, new Integer(-1968));
        processImage
                .setNumeric(RegisterRange.HOLDING_REGISTER, 17, DataType.FOUR_BYTE_INT_SIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 19, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, new Float(1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 23, DataType.EIGHT_BYTE_INT_SIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 27, DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT, new Double(1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, new Short((short) 1234));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 81, DataType.FOUR_BYTE_BCD, new Integer(12345678));

        processImage.setString(RegisterRange.HOLDING_REGISTER, 100, DataType.VARCHAR, 20, "Serotonin Software");

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12288, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12290, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                -1968.1968));

        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED, new Integer(0xfff0));
        processImage
                .setNumeric(RegisterRange.INPUT_REGISTER, 17, DataType.FOUR_BYTE_INT_UNSIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 19, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 21, DataType.FOUR_BYTE_FLOAT_SWAPPED,
                new Float(1968.1968));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 23, DataType.EIGHT_BYTE_INT_UNSIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 27, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT_SWAPPED, new Double(
                1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 50, DataType.EIGHT_BYTE_INT_UNSIGNED, 0);

        processImage.setString(RegisterRange.INPUT_REGISTER, 100, DataType.CHAR, 15, "Software de la Serotonin");

        processImage.setExceptionStatus((byte) 151);

        // Add an image listener.
        processImage.addListener(new BasicProcessImageListener());

        return processImage;
    }
}
