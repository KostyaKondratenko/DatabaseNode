package Operation;

import Connection.ConnectionNode;
import Connection.ResponseType;
import Entity.Node;
import Entity.Record;
import Interfaces.ILogger;
import Parameter.ParameterNode;
import Parameter.ParameterPort;
import Parameter.ParameterRecord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OperationGlobalProcessor {

    private OperationLocalProcessor operationProcessor;
    private List<Node> connectedNodes;
    private Node currentNode;
    private ILogger logger;

    public OperationGlobalProcessor(OperationLocalProcessor operationProcessor,
                                    List<Node> connectedNodes,
                                    Node currentNode,
                                    ILogger logger) {
        this.operationProcessor = operationProcessor;
        this.connectedNodes = connectedNodes;
        this.currentNode = currentNode;
        this.logger = logger;
    }

    public String processOperation(Operation operation) {
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
        return ResponseType.error.toString();
    }

    private String processConnectionOperation(String params) {
        Node connectNode = new ParameterNode(params).getNode();
        operationProcessor.connect(connectNode);
        return ResponseType.ok.toString();
    }

    private String processDisconnectionOperation(String params) {
        Node disconnectNode = new ParameterNode(params).getNode();
        operationProcessor.disconnect(disconnectNode);
        return ResponseType.ok.toString();
    }

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

    private String processGetValueOperation(Operation operation) {
        int key = new ParameterPort(operation.getParams()).getTcpPort();
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

    private String processFindKeyOperation(Operation operation) {
        int key = new ParameterPort(operation.getParams()).getTcpPort();
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

    private String processNewRecordOperation(String params) {
        Record record = new ParameterRecord(params).getRecord();
        operationProcessor.newRecord(record);
        return ResponseType.ok.toString();
    }

    private String processTerminateOperation(Operation operation) {
        operation.setOperationType(OperationType.disconnect);
        connectNodes(operation);
        return ResponseType.ok.toString();
    }

    private List<String> connectNodes(Operation operation) {

        List<Node> notVisited = getNotVisitedNodes(operation.getVisited());

        // Building a command
        OperationCommandBuilder builder = new OperationCommandBuilder();
        builder.addOperation(operation);
        builder.addVisitedNodes(connectedNodes);
        builder.addVisitedNode(currentNode);
        String command = builder.build();
        logger.logEvent("Command built: " + command);

        return connectToChildNodes(notVisited, command);
    }

    private List<Node> getNotVisitedNodes(List<Node> operationVisited) {
        List<Node> notVisitedChildNodes = new ArrayList<>(connectedNodes);

        for (Node visited : operationVisited) {
            notVisitedChildNodes.removeIf(node -> node.equals(visited));
        }

        return notVisitedChildNodes;
    }

    private List<String> connectToChildNodes(List<Node> notVisitedNodes, String command) {

        List<String> result = new ArrayList<>();
        for (Node node : notVisitedNodes) {
            result.add(connectToNode(node, command));
        }
        return result;
    }

    private String connectToNode(Node node, String command) {

        try {
            ConnectionNode connectionNode = new ConnectionNode(logger);
            String response = connectionNode.connectNode(node, command);
            logger.logSuccess("Child Response Received: " + response);
            return response;
        } catch (Exception e) {
            logger.logUnexpectedError(e.toString());
        }

        return ResponseType.error.toString();
    }
}