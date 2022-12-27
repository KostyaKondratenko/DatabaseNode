package Connection;

import Entity.Node;
import Interfaces.ILogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private final ILogger logger;

    public Client(ILogger logger) {
        this.logger = logger;
    }

    public String connect(Node node, String command) throws UnknownHostException, IOException {

        // Connection to a node
        Socket netSocket = new Socket(node.getAddress(), node.getPort());
        PrintWriter out = new PrintWriter(netSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(netSocket.getInputStream()));
        logger.logEvent("\nConnected to server " + netSocket.getInetAddress().getHostAddress() + ":" + netSocket.getPort());

        // Sending connection command to a node
        out.println(command);
        logger.logRequest(command);

        // Get response
        logger.logEvent("Waiting response...");
        String response = in.readLine();
        logger.logResponse(response);

        // Close all the streams and the socket
        out.close();
        in.close();
        netSocket.close();
        logger.logEvent("Disconnected from server " + netSocket.getInetAddress().getHostAddress() + ":" + netSocket.getPort() + "\n");

        // Return response
        return response;
    }
}
