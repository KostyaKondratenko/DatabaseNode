package Entity;

import Enums.OperationType;

import java.util.ArrayList;
import java.util.List;

public class Operation {
    private OperationType operationType;
    private String params;
    private List<Node> visited;

    public Operation(OperationType operationType) {
        this.operationType = operationType;
        this.params = "";
        this.visited = new ArrayList<>();
    }

    public Operation(OperationType operationType, String params, List<Node> visited) {
        this.operationType = operationType;
        this.params = params;
        this.visited = visited;
    }


    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public List<Node> getVisited() {
        return visited;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void addVisited(Node node) {
        if (!hasVisited(node)) {
            visited.add(node);
        }
    }

    private boolean hasVisited(Node node) {
        for (int i = 0; i < visited.size(); ++i) {
            if (node.equals(visited.get(i))) {
                return true;
            }
        }
        return false;
    }
}
