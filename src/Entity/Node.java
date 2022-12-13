package Entity;

public class Node {
    private final String address;
    private final int port;

    public Node(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return address + ":" + port;
    }

    public boolean equals(Node node) {
        return address == node.getAddress() && port == node.getPort();
    }
}
