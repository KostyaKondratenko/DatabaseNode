package Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationParameterList {
    private final List<Operation> operations;

    public OperationParameterList() {
        this.operations = new ArrayList<>();
    }

    public OperationParameterList(List<Operation> operations) {
        this.operations = operations;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void add(Operation operation) {
        operations.add(operation);
    }

    public boolean hasTermination() {
        for(Operation operation : operations) {
            if (operation.getOperationType() == OperationType.terminate) {
                return true;
            }
        }
        return false;
    }
}
