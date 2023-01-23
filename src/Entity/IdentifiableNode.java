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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof IdentifiableNode)) {
            if (!(obj instanceof UUID)) {
                return false;
            }
            UUID compID = (UUID) obj;
            return id.equals(compID);
        }

        IdentifiableNode compNode = (IdentifiableNode) obj;
        return compNode.getID().equals(compNode.getID());
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
