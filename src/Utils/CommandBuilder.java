package Utils;

import Entity.IdentifiableNode;
import Entity.Node;
import Entity.Operation;
import Interfaces.IIdentifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A builder for the request to a Node.
 * <p> It is build from Operation and a list of visited Nodes. </p>
 * @see Node
 * @see Operation
 */
public class CommandBuilder {

    /** A resulting request string */
    private String command;
    /** An operation to send in the request */
    private Operation operation;
    /** A list of already visited nodes to eliminate repetitions of the destination nodes. */
    private List<UUID> visited;

    public CommandBuilder() {
        visited = new ArrayList<>();
    }

    /**
     * Sets an operation to send and appends a list of visited nodes for the operation variable.
     * @param operation an operation to update.
     * @see Operation
     */
    public void addOperation(Operation operation) {
        this.operation = operation;
        operation.getVisited().forEach(this::addVisitedNode);
    }

    /**
     * Appends a node to a list of visited nodes.
     * @param node a node to append
     */
    public void addVisitedNode(IIdentifiable node) {
        addVisitedNode(node.getID());
    }

    /**
     * Appends an identifier to a list of visited nodes.
     * @param id an identifier to append
     */
    public void addVisitedNode(UUID id) {
        boolean present = false;
        for (UUID listNode : visited) {
            present = listNode.equals(id);
        }
        if (!present) {
            visited.add(id);
        }
    }

    /**
     * Appends to a list of visited nodes.
     * @param nodeList a list to append.
     */
    public void addVisitedNodes(List<IdentifiableNode> nodeList) {
        for (IIdentifiable listNode : nodeList) {
            addVisitedNode(listNode);
        }
    }

    /**
     * Builds a command in the format
     * <p> {@code <operation> [ <parameter> ] [ -visited <address:port> ]} </p>
     * @return a string line of command
     */
    public String build() {
        String delimiter = " ";
        command = operation.getOperationType().toString();
        String params = operation.getParams();
        if (params != null && !params.isEmpty()) {
            command += (delimiter + operation.getParams());
        }
        for (UUID visitedNode : visited) {
            command += (delimiter + "-visited" + delimiter + visitedNode.toString());
        }
        return command;
    }
}
