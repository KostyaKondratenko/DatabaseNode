package Utils;

import Enums.ExecutionParameterType;
import Interfaces.IParameterScanner;
import Entity.Operation;
import Operation.OperationParameterList;
import Enums.OperationType;
import Parameter.ExecutionParameterList;
import Parameter.ParameterNode;
import Parameter.ParameterInteger;
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
                    parameters.setParameterPort(new ParameterInteger(args[++i]));
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
                        break;
                    default:
                        operation.setParams(args[i]);
                        break;
                }
            }
        }

        return  parameters;
    }
    // endregion
}

