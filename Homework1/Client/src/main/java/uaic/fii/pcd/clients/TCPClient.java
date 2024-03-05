package uaic.fii.pcd.clients;

import uaic.fii.pcd.monitors.ClientMonitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient extends Client {
    public TCPClient(int port, MechanismType mechanismType, ClientMonitor clientMonitor) {
        super(port, mechanismType, clientMonitor);
    }

    @Override
    public void connectAndSend(byte[] data) {
        Socket socket = null;
        try {socket = new Socket("127.0.0.1", port);
            System.out.println("Connected to server.");

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            int offset = 0;
            long startTime = System.currentTimeMillis();
            while (offset < data.length) {
                int length = Math.min(data.length - offset, MAX_MESSAGE_SIZE);
                byte[] chunk = new byte[length];
                System.arraycopy(data, offset, chunk, 0, length);
                sendMessage(outputStream, chunk);
                if(MechanismType.STOP_AND_WAIT.equals(mechanismType)){
                    int retries = 0;
                    while(!isAckReceived(inputStream)) {
                        if(retries == Client.MAX_RETRIES) {
                            throw new RuntimeException("ACK not received. Retries: " + retries);
                        }
                        try {
                            Thread.sleep(WAIT_TIME_MILLIS_BEFORE_RETRY);
                            sendMessage(outputStream, chunk);
                            retries++;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                offset += length;
            }
            long endTime = System.currentTimeMillis();
            clientMonitor.setTransmissionTimeMillis(endTime - startTime);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(2000);
                if(socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendMessage(OutputStream outputStream, byte[] message) throws IOException {
        outputStream.write(message);
        outputStream.flush();
        clientMonitor.addToSentMessages(1);
        clientMonitor.addToSentBytes(message.length);
    }

    public boolean isAckReceived(InputStream inputStream) throws IOException {
        int ackByte = inputStream.read();
        return ackByte == ACK;
    }
}
