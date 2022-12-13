package Operation;

import Entity.Node;

import java.util.ArrayList;
import java.util.List;

public class OperationCommandBuilder {
    private String command;
    private Operation operation;
    private List<Node> visited;

    public OperationCommandBuilder() {
        visited = new ArrayList<>();
    }

    public void addOperation(Operation operation) {
        this.operation = operation;
        visited.addAll(operation.getVisited());
    }

    public void addVisitedNode(Node node) {
        visited.add(node);
    }

    public void addVisitedNodes(List<Node> nodeList) {
        visited.addAll(nodeList);
    }

    public String build() {
        String delimiter = " ";
        command = operation.getOperationType().toString() + delimiter;
        for (Node visitedNode : visited) {
            command += ("-visited" + delimiter + visitedNode.toString());
        }
        return command;
    }
}
