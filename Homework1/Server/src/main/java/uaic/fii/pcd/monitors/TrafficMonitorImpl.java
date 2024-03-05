package uaic.fii.pcd.monitors;

public class TrafficMonitor {
    private long totalBytesRead;
    private long totalMessagesReceived;

    public synchronized void incrementBytesRead(long bytesRead) {
        totalBytesRead += bytesRead;
    }

    public synchronized void incrementMessagesReceived() {
        totalMessagesReceived++;
    }

    public synchronized void displayTrafficStats() {
        System.out.println("Total Messages Received: " + totalMessagesReceived);
        System.out.println("Total Bytes Read: " + totalBytesRead);
    }
}
