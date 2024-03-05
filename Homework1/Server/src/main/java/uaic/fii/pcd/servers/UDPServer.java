package uaic.fii.pcd.servers;

import uaic.fii.pcd.monitors.TrafficMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer extends Server {
    private final int serverTimeoutAfterFirstReceivedMessageMillis;

    public UDPServer(int port, MechanismType mechanismType, TrafficMonitor trafficMonitor, int serverTimeoutAfterFirstReceivedMessageMillis) {
        super(port, Protocol.UDP, mechanismType, trafficMonitor);
        this.serverTimeoutAfterFirstReceivedMessageMillis = serverTimeoutAfterFirstReceivedMessageMillis;
    }

    @Override
    public void start() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server started on port " + port);

            byte[] buffer = new byte[MAX_MESSAGE_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
               socket.receive(packet);
               socket.setSoTimeout(serverTimeoutAfterFirstReceivedMessageMillis);
                processMessage(packet);
                if (MechanismType.STOP_AND_WAIT.equals(mechanismType)) {
                    byte[] ackData = new byte[]{ACK};
                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, packet.getAddress(), packet.getPort());
                    socket.send(ackPacket);
                }
            }
        } catch (IOException e) {
            System.out.println("Timeout of " + serverTimeoutAfterFirstReceivedMessageMillis + " reached.");
        }
    }

    private void processMessage(DatagramPacket packet) {
        String message = new String(packet.getData(), 0, packet.getLength());
        //System.out.println("Received message from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + ": " + message);
        trafficMonitor.addToMessagesRead(1);
        trafficMonitor.addToBytesRead(packet.getLength());
    }
}