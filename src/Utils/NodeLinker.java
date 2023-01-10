package Utils;

import Connection.Client;
import Entity.IdentifiableNode;
import Enums.ResponseType;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Operation;
import Interfaces.IIdentifiable;
import Parameter.ParameterNode;
import Interfaces.INodeLinker;
import Enums.OperationType;
import Interfaces.ILogger;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * A class to send a connection request to provided nodes.
 * It is used when the current node is added to a database.
 */
public class NodeLinker implements INodeLinker {
    private final List<ParameterNode> connections;
    private final IdentifiableNode currentNode;
    private final IConcurrentStorage<IdentifiableNode> storage;
    private final ILogger logger;

    public NodeLinker(List<ParameterNode> connections,
                      IdentifiableNode currentNode,
                      IConcurrentStorage<IdentifiableNode> storage,
                      ILogger logger) {
        this.connections = connections;
        this.currentNode = currentNode;
        this.storage = storage;
        this.logger = logger;
    }

    public void connectNodes() throws IOException {
        for (ParameterNode connection : connections) {
            connectNode(connection.getNode());
        }
    }

    private void connectNode(Node node) throws IOException {

        // Building a connection command
        Operation operation = new Operation(OperationType.connect);
        operation.setParams(currentNode.toString());
        CommandBuilder commandBuilder = new CommandBuilder();
        commandBuilder.addOperation(operation);
        String command = commandBuilder.build();

        // Connection to a node, sending request and receiving response
        Client client = new Client(logger);
        String response = client.connect(node, command);

        // Read the connection response
        UUID connectedNodeID = null;
        if (response != null && (connectedNodeID = UUID.fromString(response)) != null) {
            storage.add(new IdentifiableNode(connectedNodeID, node));
            logger.logEvent("Node " + node + " is connected with response " + response);
        } else {
            logger.logError("Node " + node + " is NOT connected with response " + response);
        }
    }
}


