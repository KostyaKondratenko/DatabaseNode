package Operation;

import java.util.Arrays;

public enum OperationType {
    connect("connect"),
    disconnect("connect"),

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