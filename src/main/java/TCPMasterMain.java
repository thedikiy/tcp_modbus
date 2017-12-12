import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPMasterMain {


   /* public static void main(String args[]) throws IOException {
        final String host = "localhost";
        final int portNumber = 5555;
        System.out.println("Creating socket to '" + host + "' on port " + portNumber);

        while (true) {
            Socket socket = new Socket(host, portNumber);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("server says:" + br.readLine());

            BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
            String userInput = userInputBR.readLine();

            out.println(userInput);

            System.out.println("server says:" + br.readLine());

            if ("exit".equalsIgnoreCase(userInput)) {
                socket.close();
                break;
            }
        }
    }*/

    public static void main(String[] args) {

        TCPMasterConnection con = null;
        ModbusTCPTransaction trans = null;
        ReadInputDiscretesRequest req = null;
        ReadInputDiscretesResponse res = null;
        InetAddress addr = null;
        int ref = 0;
        int count = 0;
        int repeat = 1;
        int port = 502;

        try {
            if (args.length < 3) {
                printUsage();
                System.exit(1);
            } else {
                try {
                    String astr = args[0];
                    int idx = astr.indexOf(58);
                    if (idx > 0) {
                        port = Integer.parseInt(astr.substring(idx + 1));
                        astr = astr.substring(0, idx);
                    }

                    addr = InetAddress.getByName(astr);
                    ref = Integer.parseInt(args[1]);
                    count = Integer.parseInt(args[2]);
                    if (args.length == 4) {
                        repeat = Integer.parseInt(args[3]);
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                    printUsage();
                    System.exit(1);
                }
            }

            con = new TCPMasterConnection(addr);
            con.setPort(port);
            con.connect();
            if (Modbus.debug) {
                System.out.println("Connected to " + addr.toString() + ":" + con.getPort());
            }

            req = new ReadInputDiscretesRequest(ref, count);
            req.setUnitID(0);
            if (Modbus.debug) {
                System.out.println("Request: " + req.getHexMessage());
            }

            trans = new ModbusTCPTransaction(con);
            trans.setRequest(req);
            trans.setReconnecting(false);
            int k = 0;

            do {
                trans.execute();
                res = (ReadInputDiscretesResponse) trans.getResponse();
                if (Modbus.debug) {
                    System.out.println("Response: " + res.getHexMessage());
                }

                System.out.println("Digital Inputs Status=" + res.getDiscretes().toString());
                ++k;
            } while (k < repeat);

            con.close();
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    private static void printUsage() {
        System.out.println("java net.wimpi.modbus.cmd.DITest <address{:<port>} [String]> <register [int16]> <bitcount [int16]> {<repeat [int]>}");
    }


}
