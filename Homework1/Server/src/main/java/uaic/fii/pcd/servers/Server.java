package uaic.fii.pcd.servers;

import uaic.fii.pcd.monitors.TrafficMonitor;

public abstract class Server {
    public static final int MAX_MESSAGE_SIZE = 65507;//65535;
    protected static final byte ACK = 1;
    protected final Protocol protocol;
    protected int port;
    protected MechanismType mechanismType;
    protected TrafficMonitor trafficMonitor;

    public Server(int port, Protocol protocol, MechanismType mechanismType, TrafficMonitor trafficMonitor) {
        this.port = port;
        this.protocol = protocol;
        this.mechanismType = mechanismType;
        this.trafficMonitor = trafficMonitor;
        trafficMonitor.setUsedProtocol(protocol);
    }

    public abstract void start();

    public Protocol getServerType() {
        return protocol;
    }

    public MechanismType getMechanismType() {
        return mechanismType;
    }

    public void setMechanismType(MechanismType mechanismType) {
        this.mechanismType = mechanismType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TrafficMonitor getTrafficMonitor() {
        return trafficMonitor;
    }

    public void setTrafficMonitor(TrafficMonitor trafficMonitor) {
        this.trafficMonitor = trafficMonitor;
    }
}