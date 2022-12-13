package Parameter;

import Entity.Node;

/**
 * A class that represent a single connection execution parameter.
 */
public class ParameterNode {

    // region Variables
    private final Node node;
    // endregion

    public ParameterNode(String connectionArg) throws NumberFormatException, IndexOutOfBoundsException {
        String[] gatewayArray = connectionArg.split(":");
        String address = gatewayArray[0];
        int port = Integer.parseInt(gatewayArray[1]);
        node = new Node(address, port);
    }

    // region Getters & Setters
    public Node getNode() {
        return node;
    }
    // endregion
}
