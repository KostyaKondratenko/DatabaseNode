package Connection;

import EntityList.ConcurrentNodeList;
import Interfaces.IConcurrentOperationalStorage;
import Interfaces.IConcurrentStorage;
import Entity.Node;
import Entity.Record;
import Interfaces.IParameterScanner;
import Utils.ParameterScanner;
import Operation.Operation;
import Interfaces.ILogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import Operation.OperationParameterList;
import Operation.OperationLocalProcessor;
import Operation.OperationGlobalProcessor;

public class ConnectionClient extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private IConcurrentOperationalStorage<Record> recordStorage;
    private IConcurrentStorage<Node> connectedNodes;
    private Node currentNode;

    private ILogger logger;
    private IParameterScanner parameterScanner;

    private boolean isTerminated = false;

    public ConnectionClient(Socket socket,
                            IConcurrentOperationalStorage<Record> recordStorage,
                            IConcurrentStorage<Node> connectedNodes,
                            Node currentNode,
                            ILogger logger) {
        super();
        this.socket = socket;
        this.recordStorage = recordStorage;
        this.connectedNodes = connectedNodes;
        this.currentNode = currentNode;
        this.logger = logger;
    }

    public void run() {

        try {
            logger.logEvent("Client accepted: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            while ((line = in.readLine()) != null) {
                logger.logEvent("Retrieved " + line);
                String[] args = line.split(" ");
                parameterScanner = new ParameterScanner(args);
                OperationParameterList parameterList = parameterScanner.extractOperationParameters();

                isTerminated = parameterList.hasTermination();
                for (Operation operation : parameterList.getOperations()) {
                    String result = processOperation(operation);
                    logger.logEvent("Operation Result: " + result);
                    out.println(result);
                }
            }

            // Close all the streams and the socket
            logger.logEvent("Disconnecting from node: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            out.close();
            in.close();
            socket.close();
            logger.logSuccess("Disconnected from node: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        } catch (IOException e1) {
            logger.logUnexpectedError(e1.toString());
        }
    }

    public boolean getIsTerminated() {
        return isTerminated;
    }

    private String processOperation(Operation operation) {
        OperationLocalProcessor localProcessor = new OperationLocalProcessor(recordStorage, connectedNodes);
        OperationGlobalProcessor processor = new OperationGlobalProcessor(
                localProcessor,
                ((ConcurrentNodeList) connectedNodes).getConnectedNodes(),
                currentNode,
                logger
        );
        return processor.processOperation(operation);
    }
}
