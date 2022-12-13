package Connection;

import Entity.Node;
import Interfaces.ILogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionNode {
    private final ILogger logger;

    public ConnectionNode(ILogger logger) {
        this.logger = logger;
    }

    public String connectNode(Node node, String command) throws UnknownHostException, IOException {

        // Connection to a node
        Socket netSocket = new Socket(node.getAddress(), node.getPort());
        PrintWriter out = new PrintWriter(netSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(netSocket.getInputStream()));
        logger.logSuccess("Connected to node " + netSocket.getInetAddress().getHostAddress() + ":" + netSocket.getPort());

        // Sending connection command to a node
        logger.logEvent("Sending: " + command);
        out.println(command);
        logger.logSuccess("Sent: " + command);

        // Get response
        logger.logEvent("Waiting response...");
        String response = in.readLine();
        logger.logEvent("Received " + response);

        // Close all the streams and the socket
        logger.logEvent("Disconnecting from node: " + node.getAddress() + " at port " + node.getPort());
        out.close();
        in.close();
        netSocket.close();
        logger.logSuccess("Disconnected from node: " + node.getAddress() + " at port " + node.getPort());

        // Return response
        return response;
    }
}
