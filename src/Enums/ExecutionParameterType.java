package Enums;

import Parameter.ExecutionParameterList;
import Utils.ParameterScanner;

import java.util.Arrays;

/**
 * Denotes the available execution arguments:
 * <p> tcpport
 * <p> record
 * <p> connect
 *
 * @see ExecutionParameterList
 * @see ParameterScanner
 */
public enum ExecutionParameterType {
    tcpport("-tcpport"),
    record("-record"),
    connect("-connect");

    private final String descr;

    ExecutionParameterType(String descr) {
        this.descr = descr;
    }

    @Override
    public String toString() {
        return descr;
    }

    public static ExecutionParameterType valueBy(String descr) throws IllegalArgumentException {
        return Arrays.stream(ExecutionParameterType.values())
                .filter(type -> descr.equalsIgnoreCase(type.toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No constant found"));
    }
}
