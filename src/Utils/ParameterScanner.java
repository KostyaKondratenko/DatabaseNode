package Utils;

import Execution.*;
import Interfaces.IParameterScanner;
import Operation.Operation;
import Operation.OperationParameterList;
import Operation.OperationType;
import Parameter.ParameterNode;
import Parameter.ParameterPort;
import Parameter.ParameterRecord;

import java.io.IOException;

/**
 * Extracts the parameters from the execution arguments.
 * @see IParameterScanner
 * @see ExecutionParameterList
 * @see ExecutionParameterType
 */
public class ParameterScanner implements IParameterScanner {

    // region Variables
    private String[] args;
    // endregion

    public ParameterScanner(String[] args) {
        this.args = args;
    }

    // region Getters & Setters

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    // endregion

    // region Public API
    /**
     * Returns a {@link ExecutionParameterList Parameter List class} that includes
     * program execution parameters.
     * @return Parameter List class.
     * @throws IOException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     */
    public ExecutionParameterList extractParameters() throws
            IOException,
            NumberFormatException,
            IllegalArgumentException {

        ExecutionParameterList parameters = new ExecutionParameterList();

        for(int i = 0; i < args.length; ++i) {
            ExecutionParameterType type = ExecutionParameterType.valueBy(args[i]);
            switch (type) {
                case tcpport:
                    parameters.setParameterPort(new ParameterPort(args[++i]));
                    break;
                case record:
                    parameters.setParameterRecord(new ParameterRecord(args[++i]));
                    break;
                case connect:
                    parameters.addParameterConnection(new ParameterNode(args[++i]));
                    break;
            }
        }

        return  parameters;
    }

    public OperationParameterList extractOperationParameters() throws
            IOException,
            IllegalArgumentException,
            NullPointerException {

        OperationParameterList parameters = new OperationParameterList();
        Operation operation = null;
        for(int i = 0; i < args.length; ++i) {

            try {
                OperationType type = OperationType.valueBy(args[i]);
                operation = new Operation(type);
                parameters.add(operation);
            } catch(IllegalArgumentException e) {
                switch (args[i]) {
                    case "-visited":
                        operation.addVisited(new ParameterNode(args[++i]).getNode());
                    default:
                        operation.setParams(args[i]);
                }
            }
        }

        return  parameters;
    }
    // endregion
}

