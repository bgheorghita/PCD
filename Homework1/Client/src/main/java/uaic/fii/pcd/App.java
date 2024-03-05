package uaic.fii.pcd;

import uaic.fii.pcd.clients.*;
import uaic.fii.pcd.monitors.ClientMonitor;
import uaic.fii.pcd.monitors.ClientMonitorImpl;
import uaic.fii.pcd.utils.AsciiCharacterGenerator;
import uaic.fii.pcd.utils.DataUnits;

public class App {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("To open a TCP Client: java App <PORT> TCP <STREAMING/STOP_AND_WAIT> <MB_OF_DATA_TO_SEND>");
            System.out.println("To open a UDP Client: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <MB_OF_DATA_TO_SEND> <DELAY_AFTER_SENT_MESSAGE_MILLIS>");
            System.exit(1);
        }

        int port = 0;
        try {
            String arg0 = args[0];
            if(arg0.matches("[0-9]+")) {
                port = Integer.parseInt(arg0);
                if(port < 0 || port > 65535) {
                    System.out.println("Port range must be between 0 to 65535");
                    System.exit(1);
                }
            } else {
                System.out.println("Port range must be between 0 to 65535");
                System.exit(1);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Port range must be between 0 to 65535");
            System.exit(1);
        }

        Protocol protocol = null;
        try {
            protocol = Protocol.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("The specified protocol is not available. Available protocols: UDP, TCP");
            System.exit(1);
        }

        MechanismType mechanismType = null;
        try {
            mechanismType = MechanismType.valueOf(args[2]);
        } catch (IllegalArgumentException e) {
            System.out.println("The specified mechanism type is not available. Available mechanism: STREAMING, STOP_AND_WAIT");
            System.exit(1);
        }

        ClientMonitor clientMonitor = new ClientMonitorImpl();
        Client client = null;
        if(Protocol.TCP.equals(protocol)) {
            client = new TCPClient(port, mechanismType, clientMonitor);
        } else if(Protocol.UDP.equals(protocol)) {
            if(args.length < 5) {
                System.out.println("To open a UDP Client: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <MB_OF_DATA_TO_SEND> <DELAY_AFTER_SENT_MESSAGE_MILLIS>");
                System.exit(1);
            }
            String args4 = args[4];
            if(!args4.matches("[0-9]+")) {
                System.out.println("DELAY_AFTER_SENT_MESSAGE_MILLIS parameter must contain only digits.");
                System.exit(1);
            }

            int delayAfterSentMessageMillis = Integer.parseInt(args4);
            if(delayAfterSentMessageMillis < 0) {
                System.out.println("DELAY_AFTER_SENT_MESSAGE_MILLIS must not be negative.");
                System.exit(1);
            }
            client = new UDPClient(port, mechanismType, clientMonitor, delayAfterSentMessageMillis);
        } else {
            System.out.println("To open a TCP Client: java App <PORT> TCP <STREAMING/STOP_AND_WAIT> <MB_OF_DATA_TO_SEND>");
            System.out.println("To open a UDP Client: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <MB_OF_DATA_TO_SEND> <DELAY_AFTER_SENT_MESSAGE_MILLIS>");
            System.exit(1);
        }

        String args3 = args[3];
        if(!args3.matches("[0-9]+")) {
            System.out.println("<MB_OF_DATA_TO_SEND> parameter must be a positive number.");
            System.exit(1);
        }

        byte[] data = AsciiCharacterGenerator.generateAsciiCharacters(DataUnits.ONE_MB * Integer.parseInt(args3));
        client.connectAndSend(data);

        clientMonitor.displayStatistics();
    }
}