package uaic.fii.pcd.monitors;

public interface ClientMonitor {
    void setTransmissionTimeMillis(long transmissionTimeMillis);
    void addToSentMessages(long sentMessages);
    void addToSentBytes(long sentBytes);
    void displayStatistics();
}
