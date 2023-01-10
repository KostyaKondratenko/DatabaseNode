package Parameter;

import Entity.IdentifiableNode;
import Entity.Operation;
import Enums.OperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around a list of Operations
 *
 * @see Operation
 */
public class OperationParameterList {
    private final List<Operation> operations;

    public OperationParameterList() {
        this.operations = new ArrayList<>();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void add(Operation operation) {
        operations.add(operation);
    }

    public void setServerIPForConnectionType(String ip) {
        for(Operation operation : operations) {
            if (operation.getOperationType() == OperationType.connect) {
                IdentifiableNode node = new ParameterIdentifiableNode(operation.getParams()).getNode();
                operation.setParams(
                        new IdentifiableNode(node.getID(), ip, node.getPort()).toString()
                );
            }
        }
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
