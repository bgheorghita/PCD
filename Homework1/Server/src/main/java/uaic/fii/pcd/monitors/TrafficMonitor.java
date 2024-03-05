package uaic.fii.pcd.monitors;

import uaic.fii.pcd.servers.Protocol;

public interface TrafficMonitor {
    void addToBytesRead(long bytesRead);
    void addToMessagesRead(long messagesRead);
    void setUsedProtocol(Protocol protocol);
    void displayStatistics();
}
