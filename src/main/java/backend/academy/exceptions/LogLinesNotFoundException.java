package backend.academy.exceptions;

public class LogLinesNotFoundException extends RuntimeException {
    public LogLinesNotFoundException() {
        super("Log lines not found at current path.");
    }
}
