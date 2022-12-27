import Connection.Server;
import Utils.NodeLinker;
import EntityList.ConcurrentRecordMap;
import Interfaces.*;
import EntityList.ConcurrentNodeList;
import Entity.Node;
import Entity.Record;
import Parameter.ExecutionParameterList;
import Utils.ParameterScanner;
import Utils.Logger;

import java.io.IOException;

public class DatabaseNode {

    private static IConcurrentStorage<Node> nodeStorage;
    private static IConcurrentOperationalStorage<Record> recordStorage;
    private static IParameterScanner scanner;
    private static INodeLinker connectionNodes;
    private static ILogger logger;

    public static void main(String[] args) throws IOException {

        // Initializing global variables
        recordStorage = new ConcurrentRecordMap();
        nodeStorage = new ConcurrentNodeList();
        logger = new Logger();

        // Parameter scan loop
        scanner = new ParameterScanner(args);
        ExecutionParameterList executionParams = scanner.extractParameters();

        // Starting a server
        Server server = new Server(
                executionParams.getParameterPort(),
                recordStorage,
                nodeStorage,
                logger
        );
        server.startServer();

        // Connect to nodes
        connectionNodes = new NodeLinker(
                executionParams.getParameterConnections(),
                server.getCurrentNode(),
                nodeStorage,
                logger
        );
        connectionNodes.connectNodes();

        // Adding record to a storage
        recordStorage.add(executionParams.getParameterRecord().getRecord());

        // Waiting for clients
        server.waitClient();
    }
}
