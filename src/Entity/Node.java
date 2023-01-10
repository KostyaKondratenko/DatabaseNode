package Entity;

import Utils.NetworkInformation;

import java.util.ArrayList;
import java.util.List;

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
        return equalAddresses(address, node.getAddress()) && port == node.getPort();
    }

    private boolean equalAddresses(String address, String anotherAddress) {
        if (address.equalsIgnoreCase("localhost")) {
            return "localhost".equalsIgnoreCase(anotherAddress) || "0.0.0.0".equalsIgnoreCase(anotherAddress);
        }
        return address.equalsIgnoreCase(anotherAddress);
    }
}
