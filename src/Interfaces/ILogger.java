package Interfaces;

public interface ILogger {
    void logEvent(String event);
    void logRequest(String description);
    void logResponse(String description);
    void logError(String errorDescription);
    void logUnexpectedError(String errorDescription);
}
