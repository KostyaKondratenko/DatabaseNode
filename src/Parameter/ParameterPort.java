package Parameter;

/**
 * A class that represent a tcp port execution parameter.
 */
public class ParameterPort {

    // region Variables
    private final int tcpPort;
    // endregion

    public ParameterPort(String portArg) throws NumberFormatException {
        tcpPort = Integer.parseInt(portArg);
    }

    // region Getters & Setters
    public int getTcpPort() {
        return tcpPort;
    }
    // endregion
}
