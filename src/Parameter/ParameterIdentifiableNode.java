package Parameter;


import Entity.IdentifiableNode;
import Entity.Node;

import java.util.UUID;

/**
 * A class that represent a single connection execution parameter.
 */
public class ParameterIdentifiableNode {

    // region Variables
    private final IdentifiableNode node;
    // endregion

    public ParameterIdentifiableNode(String connectionArg) throws IllegalArgumentException, IndexOutOfBoundsException {
        String[] gatewayArray = connectionArg.split("_");
        UUID id = UUID.fromString(gatewayArray[0]);
        String[] gateway = gatewayArray[1].split(":");
        String address = gateway[0];
        int port = Integer.parseInt(gateway[1]);
        node = new IdentifiableNode(id, address, port);
    }

    // region Getters & Setters
    public IdentifiableNode getNode() {
        return node;
    }
    // endregion
}
