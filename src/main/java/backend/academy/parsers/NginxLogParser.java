package backend.academy.parsers;

import backend.academy.records.LogRecord;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxLogParser {
    public final static String NGINX_LOG_PATTERN =
        "^(?<remoteAddr>\\S+)\\s+"
            + "-\\s+"
            + "(?<remoteUser>\\S+)\\s+"
            + "\\[(?<timeLocal>[^]]+)]\\s+"
            + "\"(?<request>[^\"]+)\"\\s+"
            + "(?<status>\\d{3})\\s+"
            + "(?<bodyBytesSent>\\d+)\\s+"
            + "\"(?<httpReferer>[^\"]*)\"\\s+"
            + "\"(?<httpUserAgent>[^\"]*)\"$";

    public LogRecord parse(String log, String filename) {
        Pattern pattern = Pattern.compile(NGINX_LOG_PATTERN);
        Matcher matcher = pattern.matcher(log);

        if (matcher.matches()) {
            String ip = matcher.group("remoteAddr");
            String user = matcher.group("remoteUser");
            String dateTime = matcher.group("timeLocal");
            String request = matcher.group("request");
            int status = Integer.parseInt(matcher.group("status"));
            int size = Integer.parseInt(matcher.group("bodyBytesSent"));
            String referer = matcher.group("httpReferer");
            String userAgent = matcher.group("httpUserAgent");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, formatter);

            return new LogRecord(filename, ip, user, zonedDateTime, request, status, size, referer, userAgent);
        } else {
            throw new IllegalArgumentException("Invalid log: " + log);
        }
    }
}
