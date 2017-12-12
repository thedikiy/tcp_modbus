import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.*;

public class TcpSlaveMain {

    public static void main(String[] args) {
        ModbusTCPListener listener = null;
        SimpleProcessImage spi = null;
        int port = 502;

        try {
            if (args != null && args.length == 1) {
                port = Integer.parseInt(args[0]);
            }

            System.out.println("jModbus Modbus Slave (Server)");
            spi = new SimpleProcessImage();
            spi.addDigitalOut(new SimpleDigitalOut(true));
            spi.addDigitalOut(new SimpleDigitalOut(true));
            spi.addDigitalIn(new SimpleDigitalIn(false));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addDigitalIn(new SimpleDigitalIn(false));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addDigitalIn(new SimpleDigitalIn(true));
            spi.addRegister(new SimpleRegister(251));
            spi.addInputRegister(new SimpleInputRegister(45));
            ModbusCoupler.getReference().setProcessImage(spi);
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(15);
            if (Modbus.debug) {
                System.out.println("Listening...");
            }

            listener = new ModbusTCPListener(3);
            listener.setPort(port);
            listener.start();
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }
}
