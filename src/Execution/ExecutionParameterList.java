package Execution;

import Parameter.ParameterNode;
import Parameter.ParameterPort;
import Parameter.ParameterRecord;
import Utils.ParameterScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The network node execution has the following parameters:
 * <p> {@code -tcpport <TCP port number>} denotes the number of the TCP port, which is used to wait
 * for connections from clients.
 * <p> {@code -record <key>:<value>} denotes a pair of integers being the initial values stored in this
 * node, where the first number is the key and the second is a value associated with this key.
 * There is no uniqueness requirement, both to a key and a value.
 * <p> {@code [ -connect <address>:<port> ]} denotes a list of other nodes already in the network, to
 * which this node should connect and with which it may communicate to perform operations.
 * This list is empty for the first node in the network.
 *
 * @see ParameterScanner
 */
public class ExecutionParameterList {

    // region Variables
    private ParameterPort parameterPort;
    private ParameterRecord parameterRecord;
    private List<ParameterNode> parameterNodes;
    // endregion

    public ExecutionParameterList() {
        parameterPort = null;
        parameterRecord = null;
        parameterNodes = new ArrayList<>();
    }

    // region Getters & Setters
    public void setParameterPort(ParameterPort parameterPort) {
        this.parameterPort = parameterPort;
    }

    public ParameterPort getParameterPort() {
        return parameterPort;
    }

    public void setParameterRecord(ParameterRecord parameterRecord) {
        this.parameterRecord = parameterRecord;
    }

    public ParameterRecord getParameterRecord() {
        return parameterRecord;
    }

    public void addParameterConnection(ParameterNode connection) {
        parameterNodes.add(connection);
    }

    public List<ParameterNode> getParameterConnections() {
        return parameterNodes;
    }
    // endregion

}
