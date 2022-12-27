package Utils;

import Connection.Client;
import Enums.ResponseType;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Operation;
import Operation.CommandBuilder;
import Parameter.ParameterNode;
import Interfaces.INodeLinker;
import Enums.OperationType;
import Interfaces.ILogger;

import java.io.IOException;
import java.util.List;

/**
 * A class to send a connection request to provided nodes.
 * It is used when the current node is added to a database.
 */
public class NodeLinker implements INodeLinker {
    private final List<ParameterNode> connections;
    private final Node currentNode;
    private final IConcurrentStorage<Node> storage;
    private final ILogger logger;

    public NodeLinker(List<ParameterNode> connections,
                      Node currentNode,
                      IConcurrentStorage<Node> storage,
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
        if (response != null && ResponseType.ok.hasSubstringIn(response)) {
            storage.add(node);
            logger.logEvent("Node " + node + " is connected with response " + response);
        } else {
            storage.remove(node);
            logger.logError("Node " + node + " is NOT connected with response " + response);
        }
    }
}


