package com.serotonin.modbus4j.test;

import java.util.Random;

import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;

public class ListenerTest2 {
    static Random random = new Random();
    static float ir1Value = -100;

    public static void main(String[] args) throws Exception {
        ModbusFactory modbusFactory = new ModbusFactory();
        final ModbusSlaveSet listener = modbusFactory.createTcpSlave(false);
        listener.addProcessImage(getModscanProcessImage(1));
        listener.addProcessImage(getModscanProcessImage(2));

        // When the "listener" is started it will use the current thread to run. So, if an exception is not thrown
        // (and we hope it won't be), the method call will not return. Therefore, we start the listener in a separate
        // thread so that we can use this thread to modify the values.
        new Thread(new Runnable() {
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
            updateProcessImage1((BasicProcessImage) listener.getProcessImage(1));
            updateProcessImage2((BasicProcessImage) listener.getProcessImage(2));

            synchronized (listener) {
                listener.wait(5000);
            }
        }
    }

    static void updateProcessImage1(BasicProcessImage processImage) {
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 0, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 2, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 10, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 20, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 22, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
    }

    static void updateProcessImage2(BasicProcessImage processImage) {
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 0, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 3, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 10, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 20, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 22, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 99, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 100, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 101, DataType.TWO_BYTE_INT_UNSIGNED,
                random.nextInt(10000));
    }

    static BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        processImage.setAllowInvalidAddress(true);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);
        processImage.setExceptionStatus((byte) 151);

        return processImage;
    }
}
