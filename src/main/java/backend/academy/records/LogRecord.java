package backend.academy.records;

import java.time.ZonedDateTime;

@SuppressWarnings("checkstyle.checks.sizes.RecordComponentNumberCheck")
public record LogRecord(
    String fileName,
    String remoteAddr,
    String remoteUser,
    ZonedDateTime timeLocal,
    String request,
    int status,
    long bodyBytesSent,
    String httpReferer,
    String httpUserAgent
) {
    private final static String UNKNOWN = "unknown";
    private final static String WHITESPACE_REGEX = "\\s+";

    public String resource() {
        return parseRequestForResource(request);
    }

    public String method() {
        return parseRequestForMethod(request);
    }

    private String parseRequestForMethod(String request) {
        String[] parts = request.trim().split(WHITESPACE_REGEX);
        return parts.length > 1 ? parts[0] : UNKNOWN;
    }

    private String parseRequestForResource(String request) {
        String[] parts = request.trim().split(WHITESPACE_REGEX);
        return parts.length > 1 ? parts[1] : UNKNOWN;
    }
}
