package uaic.fii.pcd.clients;

public abstract class Client {
    protected static final byte ACK = 1;
    protected static final int MAX_MESSAGE_SIZE = 65535;
    protected int port;
    protected Protocol protocol;

    public Client(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
