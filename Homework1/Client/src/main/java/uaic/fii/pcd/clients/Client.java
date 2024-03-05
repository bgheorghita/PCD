package uaic.fii.pcd.clients;

import uaic.fii.pcd.monitors.ClientMonitor;

public abstract class Client {
    protected static final byte ACK = 1;
    protected static final int WAIT_TIME_MILLIS_BEFORE_RETRY = 100;
    protected static final int MAX_RETRIES = 5;
    protected static final int MAX_MESSAGE_SIZE = 65507  ;//65535;
    protected int port;
    protected MechanismType mechanismType;
    protected ClientMonitor clientMonitor;

    public Client(int port, MechanismType mechanismType, ClientMonitor clientMonitor) {
        this.port = port;
        this.mechanismType = mechanismType;
        this.clientMonitor = clientMonitor;
    }

    public abstract void connectAndSend(byte[] data);

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MechanismType getMechanismType() {
        return mechanismType;
    }

    public void setMechanismType(MechanismType mechanismType) {
        this.mechanismType = mechanismType;
    }

    public ClientMonitor getClientMonitor() {
        return clientMonitor;
    }
}
