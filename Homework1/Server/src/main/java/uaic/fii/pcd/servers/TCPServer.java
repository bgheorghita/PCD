package uaic.fii.pcd.servers;

import uaic.fii.pcd.monitors.TrafficMonitor;
import uaic.fii.pcd.monitors.TrafficMonitorImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Server {
    public TCPServer(int port, MechanismType mechanismType, TrafficMonitor trafficMonitor) {
        super(port, Protocol.TCP, mechanismType, trafficMonitor);
    }

    public void start() {
        try {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started: " + serverSocket);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection accepted: " + socket);
                    TrafficMonitor trafficMonitor = new TrafficMonitorImpl();
                    trafficMonitor.setUsedProtocol(protocol);
                    new Thread(new ClientHandler(socket, trafficMonitor)).start();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            trafficMonitor.displayStatistics();
        }
    }

    class ClientHandler implements Runnable {
        private final Socket socket;
        private final TrafficMonitor trafficMonitor;
        final TCPServer tcpServer = TCPServer.this;

        public ClientHandler(Socket socket, TrafficMonitor trafficMonitor) {
            this.socket = socket;
            this.trafficMonitor = trafficMonitor;
        }

        @Override
        public void run() {
            try (socket) {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                byte[] buffer = new byte[Server.MAX_MESSAGE_SIZE];
                int bytesRead;
                int totalBytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    stopAndWaitIfActivated(outputStream);
                    totalBytesRead += bytesRead;
                    while (totalBytesRead < Server.MAX_MESSAGE_SIZE) {
                        int remainingBytes = Server.MAX_MESSAGE_SIZE - totalBytesRead;
                        bytesRead = inputStream.read(buffer, totalBytesRead, remainingBytes);
                        if (bytesRead == -1) {
                            break;
                        }
                        stopAndWaitIfActivated(outputStream);
                        totalBytesRead += bytesRead;
                    }
                    processMessage(buffer, totalBytesRead);
                    totalBytesRead = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Connection closed");
                trafficMonitor.displayStatistics();
            }
        }

        private void stopAndWaitIfActivated(OutputStream outputStream) throws IOException {
            if (MechanismType.STOP_AND_WAIT.equals(mechanismType)) {
                outputStream.write(ACK);
                outputStream.flush();
            }
        }

        private void processMessage(byte[] buffer, int bytesRead) {
            //String message = new String(buffer, 0, bytesRead);
            //System.out.println("Received message: " + message);
            trafficMonitor.addToMessagesRead(1);
            trafficMonitor.addToBytesRead(bytesRead);
            tcpServer.trafficMonitor.addToMessagesRead(1);
            tcpServer.trafficMonitor.addToBytesRead(bytesRead);
        }
    }
}
