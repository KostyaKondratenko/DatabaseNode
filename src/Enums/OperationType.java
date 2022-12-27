package Enums;

import Parameter.ExecutionParameterList;
import java.util.Arrays;

/**
 * Denotes the available operation types.
 * <p> The operation types for node communication: </p>
 * <p> 1. connect </p>
 * <p> 2. disconnect </p>
 * <p> The operation types for the database actions: </p>
 * <p> 1. set-value </p>
 * <p> 2. get-value </p>
 * <p> 3. find-key </p>
 * <p> 4. get-max </p>
 * <p> 5. get-min </p>
 * <p> 6. new-record </p>
 * <p> 7. terminate </p>
 *
 * @see ExecutionParameterList
 */
public enum OperationType {
    connect("connect"),
    disconnect("disconnect"),

    setValue("set-value"),
    getValue("get-value"),
    findKey("find-key"),
    getMax("get-max"),
    getMin("get-min"),
    newRecord("new-record"),
    terminate("terminate");

    private final String descr;

    OperationType(String descr) {
        this.descr = descr;
    }

    @Override
    public String toString() {
        return descr;
    }

    public static OperationType valueBy(String descr) {
        return Arrays.stream(OperationType.values())
                .filter(type -> descr.equalsIgnoreCase(type.toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant found"));
    }
}