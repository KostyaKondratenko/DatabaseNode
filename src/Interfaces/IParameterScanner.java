package Interfaces;

import Parameter.ExecutionParameterList;
import Operation.OperationParameterList;

import java.io.IOException;

/**
 * An interface for the execution parameters scanner.
 */
public interface IParameterScanner {
    ExecutionParameterList extractParameters() throws
            IOException,
            NumberFormatException,
            IllegalArgumentException;

    public OperationParameterList extractOperationParameters() throws
            IOException,
            IllegalArgumentException,
            NullPointerException;
}
