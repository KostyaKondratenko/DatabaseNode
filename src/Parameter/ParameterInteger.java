package Parameter;

/**
 * A class that represent a tcp port or a database key execution parameter.
 */
public class ParameterInteger {

    // region Variables
    private final int value;
    // endregion

    public ParameterInteger(String arg) throws NumberFormatException {
        value = Integer.parseInt(arg);
    }

    // region Getters & Setters
    public int getValue() {
        return value;
    }
    // endregion
}
