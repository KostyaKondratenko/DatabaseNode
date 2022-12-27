package Connection;

import Entity.Node;
import Entity.Record;
import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Parameter.ParameterInteger;
import Interfaces.ILogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {

    private ServerSocket server;
    private final ParameterInteger port;
    private final ILogger logger;
    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<Node> connectedNodes;

    public Server(ParameterInteger port,
                  IConcurrentOperationalStorage<Record> recordStorage,
                  IConcurrentStorage<Node> connectedNodes,
                  ILogger logger) {
        this.port = port;
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
        this.logger = logger;
    }

    public Node getCurrentNode() throws IOException {
        if (server == null) {
            logger.logError("Server is not started yet");
            throw new IOException("Server is not started yet");
        }

        String address = server.getInetAddress().getHostAddress();
        int port = server.getLocalPort();
        return new Node(address, port);
    }

    public void startServer() throws IOException {
        server = new ServerSocket(port.getValue());
        String address = server.getInetAddress().getHostAddress();
        int port = server.getLocalPort();
        logger.logEvent("Server started on " + address + ":" + port);
    }

    public void waitClient() throws IOException {
        if (server == null) {
            logger.logError("Server is not started yet");
            throw new IOException("Server is not started yet");
        }

        String address = server.getInetAddress().getHostAddress();
        int port = server.getLocalPort();
        logger.logEvent("Server waiting for client on " + address + ":" + port);

        Socket client;
        while (true) {
            try {
                client = server.accept();
            } catch (SocketException se) {
                break;
            }

            final ServerThread serverThread = new ServerThread(
                    server,
                    client,
                    recordStorage,
                    connectedNodes,
                    new Node(address, port),
                    logger
            );
            serverThread.start();
        }

        logger.logEvent("Server " + address + ":" + port + " closed");
    }
}
