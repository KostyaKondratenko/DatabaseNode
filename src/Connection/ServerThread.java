package Connection;

import EntityList.ConcurrentNodeList;
import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Record;
import Interfaces.IParameterScanner;
import Utils.ParameterScanner;
import Entity.Operation;
import Interfaces.ILogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Operation.OperationParameterList;
import Operation.LocalProcessor;
import Operation.GlobalProcessor;

public class ServerThread extends Thread {

    private ServerSocket server;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<Node> connectedNodes;
    private Node currentNode;

    private ILogger logger;
    private IParameterScanner parameterScanner;

    private boolean isTerminated = false;

    public ServerThread(ServerSocket server,
                        Socket client,
                        IConcurrentOperationalStorage<Record> recordStorage,
                        IConcurrentStorage<Node> connectedNodes,
                        Node currentNode,
                        ILogger logger) {
        super();
        this.server = server;
        this.client = client;
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
        this.currentNode = currentNode;
        this.logger = logger;
    }

    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            logger.logEvent("\nClient accepted " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

            String line;
            if ((line = in.readLine()) != null) {
                logger.logRequest(line);
                String[] args = line.split(" ");
                parameterScanner = new ParameterScanner(args);
                OperationParameterList parameterList = parameterScanner.extractOperationParameters();

                isTerminated = parameterList.hasTermination();
                for (Operation operation : parameterList.getOperations()) {
                    String result = processOperation(operation);
                    logger.logEvent("Storage: " + recordStorage.toString());
                    logger.logResponse(result);
                    out.println(result);
                }
            }

            // Close all the streams and the socket
            out.close();
            in.close();
            client.close();
            logger.logEvent("Client disconnected " + client.getInetAddress().getHostAddress() + ":" + client.getPort() + "\n");

        } catch (IOException e1) {
            logger.logUnexpectedError(e1.toString());
        }
        finally {
            if (isTerminated) {
                try {
                    server.close();
                } catch (Exception e) {
                    logger.logUnexpectedError(e.toString());
                }
            }
        }
    }

    private String processOperation(Operation operation) {
        LocalProcessor localProcessor = new LocalProcessor(recordStorage, connectedNodes);
        GlobalProcessor processor = new GlobalProcessor(
                localProcessor,
                ((ConcurrentNodeList) connectedNodes).getConnectedNodes(),
                currentNode,
                logger
        );
        return processor.processOperation(operation);
    }
}
