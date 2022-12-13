package Utils;

import Interfaces.ILogger;

import java.util.Arrays;

public class Logger implements ILogger {
    @Override
    public void logEvent(String event) {
        System.out.println(event);
    }

    @Override
    public void logSuccess(String description) {
        final String delimiter = delimiter(description);
        System.out.println("OK" + delimiter + description);
    }

    @Override
    public void logError(String errorDescription) {
        final String delimiter = delimiter(errorDescription);
        System.out.println("ERROR" + delimiter + errorDescription);
    }

    @Override
    public void logUnexpectedError(String errorDescription) {
        final StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        final String delimiter = "\n";
        System.out.println("ERROR: " + errorDescription + delimiter + Arrays.toString(stack));
    }

    private String delimiter(String description) {
        return description.isEmpty() ? "" : ": ";
    }
}
