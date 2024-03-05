package uaic.fii.pcd.monitors;

import uaic.fii.pcd.utils.DataUnits;

public class ClientMonitorImpl implements ClientMonitor {
    private long transmissionTimeMillis;
    private long sentMessages;
    private long sentBytes;

    @Override
    public void setTransmissionTimeMillis(long transmissionTimeMillis) {
        this.transmissionTimeMillis = transmissionTimeMillis;
    }

    @Override
    public void addToSentMessages(long sentMessages) {
        this.sentMessages += sentMessages;
    }

    @Override
    public void addToSentBytes(long sentBytes) {
        this.sentBytes += sentBytes;
    }

    @Override
    public void displayStatistics() {
        System.out.println("Transmission time: " + transmissionTimeMillis + " millis");
        System.out.println("Number of sent messages: " + sentMessages);
        System.out.println("Number of bytes sent: " + sentBytes + " (" + sentBytes / (sentBytes == 0 ? 1 : DataUnits.ONE_MB) + " MB)");
    }
}
