package gnu.io;

public class RXTXHack {
    private RXTXHack() {
        // Singleton
    }

    public static void closeRxtxPort(RXTXPort port) {
        try {
            port.IOLocked = 0;
        }
        catch (IllegalAccessError e) {
            System.out.println(e.getMessage());
        }
        port.close();
    }
}
