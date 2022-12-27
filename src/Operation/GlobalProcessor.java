package Operation;

import Connection.Client;
import Entity.Operation;
import Enums.OperationType;
import Enums.ResponseType;
import Entity.Node;
import Entity.Record;
import Interfaces.ILogger;
import Parameter.ParameterNode;
import Parameter.ParameterInteger;
import Parameter.ParameterRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class that sends a requests to a neighbour nodes to get the result of operation.
 *
 * @see LocalProcessor
 * @see Operation
 * @see OperationType
 */
public class GlobalProcessor {

    /**
     * A local processor that handles the operation locally for the current node.
     * @see Operation
     * @see LocalProcessor
     */
    private LocalProcessor operationProcessor;
    /**
     * A list of connected, neighbouring nodes to a current node.
     * @see Node
     */
    private List<Node> connectedNodes;
    /** A current node. */
    private Node currentNode;
    /** A simple event logger. */
    private ILogger logger;

    public GlobalProcessor(LocalProcessor operationProcessor,
                           List<Node> connectedNodes,
                           Node currentNode,
                           ILogger logger) {
        this.operationProcessor = operationProcessor;
        this.connectedNodes = connectedNodes;
        this.currentNode = currentNode;
        this.logger = logger;
    }

    /**
     * Operation processing locally and by sending requests to a connected nodes.
     * @param operation an operation to process.
     * @return a operation response. It could be OK, ERROR, {@code <key:value>}
     * or {@code <address>:<port>} message.
     * @see Operation
     */
    public String processOperation(Operation operation) {
        try {
            switch (operation.getOperationType()) {
                case connect:
                    return processConnectionOperation(operation.getParams());
                case disconnect:
                    return processDisconnectionOperation(operation.getParams());
                case setValue:
                    return processSetValueOperation(operation);
                case getValue:
                    return processGetValueOperation(operation);
                case findKey:
                    return processFindKeyOperation(operation);
                case getMax:
                    return processGetMaxOperation(operation);
                case getMin:
                    return processGetMinOperation(operation);
                case newRecord:
                    return processNewRecordOperation(operation.getParams());
                case terminate:
                    return processTerminateOperation(operation);
            }
        } catch (Exception e) {
            logger.logUnexpectedError(e.toString());
        }

        return ResponseType.error.toString();
    }

    /**
     * Processes a connection operation that connects a node provided in the request parameters
     * to a current node.
     * @param params a string parameter that should contain an {@code <address:port>}
     *               pair of a Node to connect.
     * @return The result of this operation is OK response.
     * @see OperationType
     * @see Node
     */
    private String processConnectionOperation(String params) {
        Node connectNode = new ParameterNode(params).getNode();
        operationProcessor.connect(connectNode);
        return ResponseType.ok.toString();
    }

    /**
     * Processes a disconnect operation that disconnects a node provided in the request
     * parameters from a current node.
     * @param params a string parameter that should contain an {@code <address:port>}
     *               pair of a Node to disconnect.
     * @return The result of this operation is OK response.
     * @see OperationType
     * @see Node
     */
    private String processDisconnectionOperation(String params) {
        Node disconnectNode = new ParameterNode(params).getNode();
        operationProcessor.disconnect(disconnectNode);
        return ResponseType.ok.toString();
    }

    /**
     * Sets a value to an existing record in the database to a value provided in request parameters.
     * Firstly, a new value is sets locally to the current node.
     * If the operation succeeded - OK result is returned.
     * Otherwise, a request with the set-value operation is sent to connected nodes.
     * If at least one neighbour node returned OK result, then OK is returned, ERROR - otherwise.
     * @param operation an operation to process.
     * @return The result of this operation is OK or ERROR response
     * @see Operation
     */
    private String processSetValueOperation(Operation operation) {
        Record setRecord = new ParameterRecord(operation.getParams()).getRecord();
        boolean result = operationProcessor.setValue(setRecord);
        if (result) {
            return ResponseType.ok.toString();
        } else {
            List<String> childResults = connectNodes(operation);
            for (String childResult : childResults) {
                if (ResponseType.ok.hasSubstringIn(childResult)) {
                    return ResponseType.ok.toString();
                }
            }
            return ResponseType.error.toString();
        }
    }

    /**
     * Gets a value to an existing record in the database by the provided key in request parameters.
     * Firstly, a value is searched locally in the current node.
     * If the operation succeeded - {@code <key>:<value>} result is returned.
     * Otherwise, a request with the get-value operation is sent to connected nodes.
     * If at least one neighbour node returned {@code <key>:<value>} result,
     * then neighbour {@code <key>:<value>} is returned, ERROR - otherwise.
     * @param operation an operation to process.
     * @return The result of this operation is a message consisting of a pair {@code <key>:<value>}
     * if operation succeeded or ERROR if the database contains no pair with a requested key value.
     * @see Operation
     */
    private String processGetValueOperation(Operation operation) {
        int key = new ParameterInteger(operation.getParams()).getValue();
        Record result = operationProcessor.getValue(key);
        if (result != null) {
            return result.toString();
        } else {
            List<String> childResults = connectNodes(operation);
            for (String childResult : childResults) {
                if (!ResponseType.error.hasSubstringIn(childResult)) {
                    return childResult;
                }
            }
            return ResponseType.error.toString();
        }
    }

    /**
     * Finds an existing record in the database by the provided key in request parameters.
     * Firstly, a value is searched locally in the current node.
     * If the operation succeeded - {@code <address>:<port>} of the current node is returned.
     * Otherwise, a request with the find-key operation is sent to connected nodes.
     * If at least one neighbour node returned its {@code <address>:<port>} result,
     * then neighbour's {@code <address>:<port>} is returned, ERROR - otherwise.
     * @param operation an operation to process.
     * @return The result of this operation is a message consisting of a pair {@code <address>:<port>}
     * if operation succeeded or ERROR if the database contains no value with a requested key.
     * @see Operation
     */
    private String processFindKeyOperation(Operation operation) {
        int key = new ParameterInteger(operation.getParams()).getValue();
        boolean result = operationProcessor.findKey(key);
        if (result) {
            return currentNode.toString();
        } else {
            List<String> childResults = connectNodes(operation);
            for (String childResult : childResults) {
                if (!ResponseType.error.hasSubstringIn(childResult)) {
                    return childResult;
                }
            }
            return ResponseType.error.toString();
        }
    }

    /**
     * Finds a maximum value in the database.
     * A maximum value is searched locally an added to
     * a list of maximum values.
     * Then get-max request is sent to all neighbours that was not visited previously.
     * The maximum records from neighbours are added to the list of maximum values.
     * Then maximum is retrieved from the list of values from all nodes.
     * @param operation an operation to process.
     * @return The result is a pair consisting of {@code <key>:<value>} pair or ERROR if the database has no records.
     * @see Operation
     */
    private String processGetMaxOperation(Operation operation) {
        Record result = operationProcessor.getMax();
        List<Record> results = new ArrayList<>();
        if (result != null) {
            results.add(result);
        }
        List<String> childResults = connectNodes(operation);
        for (String childResult : childResults) {
            if (!ResponseType.error.hasSubstringIn(childResult)) {
                ParameterRecord paramRecord = new ParameterRecord(childResult);
                Record record = paramRecord.getRecord();
                if (result != null) {
                    results.add(record);
                }
            }
        }
        Comparator<Record> comparator = Comparator.comparing(Record::getValue);
        return results.isEmpty() ? ResponseType.error.toString() : results.stream().max(comparator).orElse(result).toString();
    }

    /**
     * Finds a minimum value in the database.
     * A minimum value is searched locally an added to
     * a list of minimum values.
     * Then get-min request is sent to all neighbours that was not visited previously.
     * The minimum records from neighbours are added to the list of minimum values.
     * Then minimum is retrieved from the list of values from all nodes.
     * @param operation an operation to process.
     * @return The result is a pair consisting of {@code <key>:<value>} pair or ERROR if the database has no records.
     * @see Operation
     */
    private String processGetMinOperation(Operation operation) {
        Record result = operationProcessor.getMin();
        List<Record> results = new ArrayList<>();
        if (result != null) {
            results.add(result);
        }
        List<String> childResults = connectNodes(operation);
        for (String childResult : childResults) {
            if (!ResponseType.error.hasSubstringIn(childResult)) {
                ParameterRecord paramRecord = new ParameterRecord(childResult);
                Record record = paramRecord.getRecord();
                if (result != null) {
                    results.add(record);
                }
            }
        }
        Comparator<Record> comparator = Comparator.comparing(Record::getValue);
        return results.isEmpty() ? ResponseType.error.toString() : results.stream().min(comparator).orElse(result).toString();
    }

    /**
     * Remembers a new pair {@code <key:value>} instead of the pair currently
     * stored in the node to which the client is connected.
     * @param params a string parameter that should contain an {@code <key:value>}
     *               pair of a record to store.
     * @return The result of this operation is the OK message.
     * @see Record
     */
    private String processNewRecordOperation(String params) {
        Record record = new ParameterRecord(params).getRecord();
        operationProcessor.newRecord(record);
        return ResponseType.ok.toString();
    }

    /**
     * Processes termination operation.
     * Sends to all connected nodes a disconnect operation
     * with current node's {@code <address:port>} pair.
     * @param operation an operation to process
     * @return The result of this operation is the OK message.
     * @see Operation
     */
    private String processTerminateOperation(Operation operation) {
        operation.setOperationType(OperationType.disconnect);
        operation.setParams(currentNode.toString());
        connectNodes(operation);
        return ResponseType.ok.toString();
    }

    /**
     * Connecting to neighbouring nodes.
     * <p> Finding out a list of not yen visited nodes. </p>
     * <p> Building a command to send to not visited neighbouring nodes. </p>
     * <p> Adding a current operation and visited nodes to a command string </p>
     * @param operation an operation to send to connected nodes
     * @return a list of responses from connected nodes.
     */
    private List<String> connectNodes(Operation operation) {

        List<Node> notVisited = getNotVisitedNodes(operation.getVisited());

        // Building a command
        CommandBuilder builder = new CommandBuilder();
        builder.addOperation(operation);
        builder.addVisitedNodes(connectedNodes);
        builder.addVisitedNode(currentNode);
        String command = builder.build();

        return connectToChildNodes(notVisited, command);
    }

    /**
     * Computes a list on not visited nodes by filtering connected nodes
     * if the node was provided in the request of the operation.
     * @param operationVisited an operation that contains a list of already visited nodes.
     * @return a list of not visited nodes.
     */
    private List<Node> getNotVisitedNodes(List<Node> operationVisited) {
        List<Node> notVisitedChildNodes = new ArrayList<>(connectedNodes);

        for (Node visited : operationVisited) {
            notVisitedChildNodes.removeIf(node -> node.equals(visited));
        }

        return notVisitedChildNodes;
    }

    /**
     * Connects and sends a provided string line to a list of nodes.
     * @param notVisitedNodes a list of nodes to send a string command.
     * @param command a string line to send to all provided nodes.
     * @return a list of responses from the connected nodes.
     * @see Node
     */
    private List<String> connectToChildNodes(List<Node> notVisitedNodes, String command) {

        List<String> result = new ArrayList<>();
        for (Node node : notVisitedNodes) {
            result.add(connectToNode(node, command));
        }
        return result;
    }

    /**
     * Send a provided string to one specified node.
     * @param node a node to send request.
     * @param command a string line to send to specified node.
     * @return a response from node.
     * @see Node
     */
    private String connectToNode(Node node, String command) {

        try {
            Client client = new Client(logger);
            String response = client.connect(node, command);
            return response;
        } catch (Exception e) {
            logger.logUnexpectedError(e.toString());
        }

        return ResponseType.error.toString();
    }
}