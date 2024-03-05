package uaic.fii.pcd.clients;

import uaic.fii.pcd.monitors.ClientMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends Client {
    private final int delayAfterSentMessageMillis;

    public UDPClient(int port, MechanismType mechanismType, ClientMonitor clientMonitor, int delayAfterSentMessageMillis) {
        super(port, mechanismType, clientMonitor);
        this.delayAfterSentMessageMillis = delayAfterSentMessageMillis;
    }

    @Override
    public void connectAndSend(byte[] data) {
        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("Connected to server.");

            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");

            int offset = 0;
            long startTime = System.currentTimeMillis();
            while (offset < data.length) {
                int length = Math.min(data.length - offset, MAX_MESSAGE_SIZE);
                byte[] chunk = new byte[length];
                System.arraycopy(data, offset, chunk, 0, length);

                DatagramPacket sendPacket = new DatagramPacket(chunk, chunk.length, serverAddress, port);
                socket.send(sendPacket);

                if (MechanismType.STOP_AND_WAIT.equals(mechanismType)) {
                    byte[] ackData = new byte[]{ACK};
                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length);
                    socket.setSoTimeout(WAIT_TIME_MILLIS_BEFORE_RETRY);
                    int retries = 0;
                    boolean ackReceived = false;
                    while (!ackReceived && retries < MAX_RETRIES) {
                        try {
                            socket.receive(ackPacket);
                            if (ackPacket.getData()[0] == ACK) {
                                ackReceived = true;
                            } else {
                                socket.send(sendPacket);
                                retries++;
                            }
                        } catch (IOException e) {
                            socket.send(sendPacket);
                            retries++;
                        }
                    }
                    if (!ackReceived) {
                        throw new RuntimeException("ACK not received after " + Client.MAX_RETRIES + " retries");
                    }
                }

                offset += length;
                clientMonitor.addToSentMessages(1);
                clientMonitor.addToSentBytes(length);

                try {
                    Thread.sleep(delayAfterSentMessageMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            long endTime = System.currentTimeMillis();
            clientMonitor.setTransmissionTimeMillis(endTime - startTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}