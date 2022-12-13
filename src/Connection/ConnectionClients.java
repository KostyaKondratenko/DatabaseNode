package Connection;

import Entity.Node;
import Entity.Record;
import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Parameter.ParameterPort;
import Interfaces.ILogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionClients {

    private final ParameterPort port;
    private final ILogger logger;
    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<Node> connectedNodes;

    public ConnectionClients(ParameterPort port,
                             IConcurrentOperationalStorage<Record> recordStorage,
                             IConcurrentStorage<Node> connectedNodes,
                             ILogger logger) {
        this.port = port;
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
        this.logger = logger;
    }

    public void waitClient() throws IOException {

        ServerSocket server = new ServerSocket(port.getTcpPort());
        String address = server.getInetAddress().getHostAddress();
        int port = server.getLocalPort();
        logger.logEvent("Server listens on: " + address + ":" + port);

        Socket client = null;
        final Boolean[] isTerminated = {false};
        while (!isTerminated[0]) {
            client = server.accept();
            final ConnectionClient connectionClient = new ConnectionClient(
                    client,
                    recordStorage,
                    connectedNodes,
                    new Node(address, port),
                    logger
            );
            final Thread connectionClientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectionClient.run();
                    }
                    finally {
                        isTerminated[0] = connectionClient.getIsTerminated();
                    }
                }
            });
            connectionClientThread.start();
        }

        // Close server after termination command
        logger.logEvent("Closing server: " + address + ":" + port);
        server.close();
        logger.logSuccess("Server closed: " + address + ":" + port);
    }
}
