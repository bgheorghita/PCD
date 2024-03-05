package uaic.fii.pcd;

import uaic.fii.pcd.monitors.TrafficMonitor;
import uaic.fii.pcd.monitors.TrafficMonitorImpl;
import uaic.fii.pcd.servers.*;

public class App {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("To open a TCP Server: java App <PORT> TCP <STREAMING/STOP_AND_WAIT>");
            System.out.println("To open a UDP Server: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <TIMEOUT_AFTER_FIRST_RECEIVED_MESSAGE_MILLIS>");
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

        TrafficMonitor trafficMonitor = new TrafficMonitorImpl();
        Server server = null;
        if(Protocol.TCP.equals(protocol)) {
            server = new TCPServer(port, mechanismType, trafficMonitor);
        } else if(Protocol.UDP.equals(protocol)) {
            if(args.length < 4) {
                System.out.println("To open a UDP Server: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <TIMEOUT_AFTER_FIRST_RECEIVED_MESSAGE_MILLIS>");
                System.exit(1);
            }
            String args3 = args[3];
            if(!args3.matches("[0-9]+")) {
                System.out.println("TIMEOUT_AFTER_FIRST_RECEIVED_MESSAGE_MILLIS parameter must contain only digits.");
                System.exit(1);
            }

            int serverTimeoutAfterFirstReceivedMessageMillis = Integer.parseInt(args[3]);
            if(serverTimeoutAfterFirstReceivedMessageMillis <= 0) {
                System.out.println("TIMEOUT_AFTER_FIRST_RECEIVED_MESSAGE_MILLIS must be positive. Needed for stopping the server and display statistics.");
                System.exit(1);
            }
            server = new UDPServer(port, mechanismType, trafficMonitor, serverTimeoutAfterFirstReceivedMessageMillis);
        } else {
            System.out.println("To open a TCP Server: java App <PORT> TCP <STREAMING/STOP_AND_WAIT>");
            System.out.println("To open a UDP Server: java App <PORT> UDP <STREAMING/STOP_AND_WAIT> <TIMEOUT_AFTER_FIRST_RECEIVED_MESSAGE_MILLIS>");
            System.exit(1);
        }

        server.start();
        trafficMonitor.displayStatistics();
    }
}