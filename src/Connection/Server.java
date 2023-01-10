package Connection;

import Entity.IdentifiableNode;
import Entity.Node;
import Entity.Record;
import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Interfaces.IIdentifiable;
import Parameter.ParameterInteger;
import Interfaces.ILogger;
import Utils.NetworkInformation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

public class Server implements IIdentifiable {

    private ServerSocket server;
    private final ParameterInteger port;
    private final ILogger logger;
    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<IdentifiableNode> connectedNodes;
    private static UUID id = UUID.randomUUID();

    public Server(ParameterInteger port,
                  IConcurrentOperationalStorage<Record> recordStorage,
                  IConcurrentStorage<IdentifiableNode> connectedNodes,
                  ILogger logger) {
        this.port = port;
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
        this.logger = logger;
    }

    @Override
    public UUID getID() {
        return id;
    }

    public IdentifiableNode getCurrentNode() throws IOException {
        if (server == null) {
            logger.logError("Server is not started yet");
            throw new IOException("Server is not started yet");
        }

        String address = server.getInetAddress().getHostAddress();
        int port = server.getLocalPort();
        return new IdentifiableNode(id, address, port);
    }

    public void startServer() throws IOException {
        server = new ServerSocket(port.getValue());
        int port = server.getLocalPort();
        logger.logEvent("Server started on port " + port);
    }

    public void waitClient() throws IOException {
        if (server == null) {
            logger.logError("Server is not started yet");
            throw new IOException("Server is not started yet");
        }

        int port = server.getLocalPort();
        logger.logEvent("Server waiting for client on port " + port);

        String networkInformation = new NetworkInformation().getNetworkInformation();
        logger.logEvent(networkInformation);

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
                    getCurrentNode(),
                    logger
            );
            serverThread.start();
        }

        logger.logEvent("Server " + getCurrentNode().toString() + " closed");
    }
}
