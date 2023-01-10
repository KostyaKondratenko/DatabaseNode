package Parameter;

import java.util.UUID;

/**
 * A class that represent an identifier of a connected node in operation or connection parameter.
 */
public class ParameterID {

    // region Variables
    private final UUID id;
    // endregion

    public ParameterID(String arg) throws IllegalArgumentException {
        id = UUID.fromString(arg);
    }

    // region Getters & Setters
    public UUID getID() {
        return id;
    }
    // endregion
}