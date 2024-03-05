package uaic.fii.pcd.monitors;

import uaic.fii.pcd.servers.Protocol;
import uaic.fii.pcd.utils.DataUnits;

public class TrafficMonitorImpl implements TrafficMonitor {
    private long totalBytesRead;
    private long totalMessagesReceived;
    private Protocol usedProtocol;

    @Override
    public synchronized void addToBytesRead(long bytesRead) {
        totalBytesRead += bytesRead;
    }

    @Override
    public synchronized void addToMessagesRead(long messagesRead) {
        totalMessagesReceived += messagesRead;
    }

    @Override
    public synchronized void setUsedProtocol(Protocol protocol) {
        this.usedProtocol = protocol;
    }

    @Override
    public void displayStatistics() {
        if(usedProtocol != null) {
            System.out.println("Used protocol: " + usedProtocol.name());
        }
        System.out.println("Total Messages Received: " + totalMessagesReceived);
        System.out.println("Total Bytes Read: " + totalBytesRead + " (" + totalBytesRead / (totalBytesRead == 0 ? 1 : DataUnits.ONE_MB) + " MB)");
    }
}
