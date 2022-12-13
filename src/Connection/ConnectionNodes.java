package Connection;

import Interfaces.IConcurrentStorage;
import Entity.Node;
import Parameter.ParameterNode;
import Interfaces.IConnectionNodes;
import Operation.OperationType;
import Interfaces.ILogger;

import java.io.IOException;
import java.util.List;

public class ConnectionNodes implements IConnectionNodes {
    private final List<ParameterNode> connections;
    private final IConcurrentStorage<Node> storage;
    private final ILogger logger;

    public ConnectionNodes(List<ParameterNode> connections, IConcurrentStorage<Node> storage, ILogger logger) {
        this.connections = connections;
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
        OperationType commandType = OperationType.connect;
        String address = node.getAddress();
        int port = node.getPort();
        String command = commandType + " " + address + ":" + port;

        // Connection to a node, sending request and receiving response
        ConnectionNode connectionNode = new ConnectionNode(logger);
        String response = connectionNode.connectNode(node, command);

        // Read the connection response
        if (response != null && ResponseType.ok.hasSubstringIn(response)) {
            storage.add(node);
            logger.logSuccess("Node " + address + ":" + port + " is connected with response " + response);
        } else {
            storage.remove(node);
            logger.logError("Node " + address + ":" + port + " is NOT connected with response " + response);
        }
    }
}


