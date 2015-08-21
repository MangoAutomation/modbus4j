package com.serotonin.modbus4j.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.serotonin.modbus4j.sero.io.StreamUtils;

public class Test {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(502);
        while (true)
            new SocketThread(ss.accept());
    }
    
    static class SocketThread extends Thread {
        Socket s;
        InputStream in;
        SocketThread(Socket s) {
            System.out.println("Socket opened");
            try {
                this.s = s;
                this.in = s.getInputStream();
                start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            byte[] b = new byte[1024];
            int readcount;
            try {
                while (true) {
                    readcount = in.read(b);
                    if (readcount == -1)
                        break;
                    System.out.println(StreamUtils.dumpMessage(b, 0, readcount));
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
            
            try {
                if (s != null)
                    s.close();
            }
            catch (IOException e) {}
            
            System.out.println("Socket closed");
        }
    }
    
//    public static void main(String[] args) throws Exception {
//        SerialParameters serialParameters = new SerialParameters();
//        serialParameters.setCommPortId("COM1");
//        serialParameters.setPortOwnerName("Numb nuts");
//        serialParameters.setBaudRate(9600);
//        
//        ModbusFactory modbusFactory = new ModbusFactory();
//        
//        ModbusMaster master = modbusFactory.createRtuMaster(serialParameters, false);
//        System.out.println("init 1");
//        master.init();
//        System.out.println("destroy 1");
//        master.destroy();
//        
//        master = modbusFactory.createRtuMaster(serialParameters, false);
//        System.out.println("init 2");
//        master.init();
//        System.out.println("destroy 2");
//        master.destroy();
//    }
}
