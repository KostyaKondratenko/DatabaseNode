package Entity;

import Interfaces.IIdentifiable;

import java.util.UUID;

public class IdentifiableNode extends Node implements IIdentifiable {
    private UUID id;

    public IdentifiableNode(UUID id, String address, int port) {
        super(address, port);
        this.id = id;
    }

    public IdentifiableNode(UUID id, Node node) {
        super(node.getAddress(), node.getPort());
        this.id = id;
    }

    public boolean equals(IdentifiableNode node) {
        return id.equals(node.id);
    }

    public boolean equals(UUID id) {
        return id.equals(id);
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString().toUpperCase() + "_" + super.toString();
    }
}
