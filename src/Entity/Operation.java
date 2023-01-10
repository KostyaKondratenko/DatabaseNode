package Entity;

import Enums.OperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Operation {
    private OperationType operationType;
    private String params;
    private List<UUID> visited;

    public Operation(OperationType operationType) {
        this.operationType = operationType;
        this.params = "";
        this.visited = new ArrayList<>();
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public List<UUID> getVisited() {
        return visited;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void addVisited(UUID node) {
        if (!hasVisited(node)) {
            visited.add(node);
        }
    }

    private boolean hasVisited(UUID node) {
        for (int i = 0; i < visited.size(); ++i) {
            if (node.equals(visited.get(i))) {
                return true;
            }
        }
        return false;
    }
}
