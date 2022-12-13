import Connection.ConnectionClients;
import Connection.ConnectionNodes;
import EntityList.ConcurrentRecordMap;
import Interfaces.*;
import EntityList.ConcurrentNodeList;
import Entity.Node;
import Entity.Record;
import Execution.ExecutionParameterList;
import Utils.ParameterScanner;
import Utils.Logger;

import java.io.IOException;

public class DatabaseNode {

    private static IConcurrentStorage<Node> nodeStorage;
    private static IConcurrentOperationalStorage<Record> recordStorage;
    private static IParameterScanner scanner;
    private static IConnectionNodes connectionNodes;
    private static ILogger logger = new Logger();

    public static void main(String[] args) throws IOException {

        // Parameter scan loop
        scanner = new ParameterScanner(args);
        ExecutionParameterList executionParams = scanner.extractParameters();

        // Connect to nodes
        nodeStorage = new ConcurrentNodeList();
        connectionNodes = new ConnectionNodes(
                executionParams.getParameterConnections(),
                nodeStorage,
                logger
        );
        connectionNodes.connectNodes();

        // Starting a server and waiting for clients
        recordStorage = new ConcurrentRecordMap();
        ConnectionClients connectionClients = new ConnectionClients(
                executionParams.getParameterPort(),
                recordStorage,
                nodeStorage,
                logger
        );
        connectionClients.waitClient();
    }
}
