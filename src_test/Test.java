import com.serotonin.modbus4j.ip.encap.EncapMessageParser;
import com.serotonin.modbus4j.sero.messaging.IncomingMessage;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class Test {
    //    public static void main(String[] args) {
    //        NumericLocator l = new NumericLocator(0, 0, 0, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED);
    //        System.out.println(l.bytesToValue(new byte[] { (byte) 0xE8, 0x03, 0x00, 0x00 }, 0));
    //    }

    public static void main(String[] args) throws Exception {
        {
            ByteQueue b = new ByteQueue("01040000000271cb");
            EncapMessageParser parser = new EncapMessageParser(false);
            IncomingMessage m = parser.parseMessage(b);
            System.out.println(m);
        }
        {
            ByteQueue b = new ByteQueue("01040404d2162ed4f1");
            EncapMessageParser parser = new EncapMessageParser(true);
            IncomingMessage m = parser.parseMessage(b);
            System.out.println(m);
        }
    }

    //    public static void main(String[] args) throws Exception {
    //        ModbusFactory factory = new ModbusFactory();
    //        IpParameters params = new IpParameters();
    //
    //        //        params.setHost("127.0.0.1");
    //        //        params.setPort(502);
    //        //        params.setEncapsulated(true);
    //
    //        params.setHost("10.1.10.10");
    //        params.setPort(502);
    //        params.setEncapsulated(false);
    //
    //        ModbusMaster master = factory.createTcpMaster(params, true);
    //        // master.setRetries(4);
    //        master.setTimeout(2000);
    //        master.setRetries(0);
    //
    //        long start = System.currentTimeMillis();
    //        try {
    //            master.init();
    //            for (int i = 0; i < 100; i++) {
    //                System.out.println(master.getValue(new NumericLocator(1, RegisterRange.HOLDING_REGISTER, 11,
    //                        DataType.TWO_BYTE_INT_SIGNED)));
    //            }
    //        }
    //        finally {
    //            master.destroy();
    //        }
    //
    //        System.out.println("Took: " + (System.currentTimeMillis() - start) + "ms");
    //    }

    // public static void main(String[] args) throws Exception {
    // ModbusFactory factory = new ModbusFactory();
    // IpParameters params = new IpParameters();
    // params.setHost("localhost");
    // params.setPort(12345);
    // ModbusMaster master = factory.createTcpMaster(params, true, false);
    // // master.setRetries(4);
    // master.setRetries(0);
    // try {
    // master.init();
    // master.getValue(1, RegisterRange.HOLDING_REGISTER, 0, DataType.TWO_BYTE_INT_UNSIGNED);
    // }
    // finally {
    // master.destroy();
    // }
    // }
}
