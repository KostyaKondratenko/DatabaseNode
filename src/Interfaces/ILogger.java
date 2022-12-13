package Interfaces;

public interface ILogger {
    void logEvent(String event);
    void logSuccess(String description);
    void logError(String errorDescription);
    void logUnexpectedError(String errorDescription);
}
