package Parameter;

import Entity.Record;

/**
 * A class that represent record execution parameter.
 */
public class ParameterRecord {

    // region Variables
    private final Record record;
    // endregion

    public ParameterRecord(String recordArg) throws NumberFormatException, IndexOutOfBoundsException {
        String[] gatewayArray = recordArg.split(":");
        int key = Integer.parseInt(gatewayArray[0]);
        int value = Integer.parseInt(gatewayArray[1]);
        record = new Record(key, value);
    }

    // region Getters & Setters
    public Record getRecord() {
        return record;
    }
    // endregion
}
