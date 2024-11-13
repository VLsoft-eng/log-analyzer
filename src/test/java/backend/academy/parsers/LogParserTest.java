package backend.academy.parsers;

import backend.academy.records.LogRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogParserTest {

    @DisplayName("NginxLogParser Test with valid log string")
    @Test
    public void NginxLogParserTestValidLog() {
        NginxLogParser parser = new NginxLogParser();
        String logLine = "93.180.71.3 - - [17/May/2015:08:05:32 +0000] " +
            "\"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" " +
            "\"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        Assertions.assertDoesNotThrow(() -> {
            LogRecord logRecord = parser.parse(logLine, "log.txt");

            Assertions.assertAll(
                () -> assertEquals("log.txt", logRecord.fileName()),
                () -> assertEquals("93.180.71.3", logRecord.remoteAddr()),
                () -> assertEquals("-", logRecord.remoteUser()),
                () -> assertEquals(ZonedDateTime.parse("2015-05-17T08:05:32+00:00"), logRecord.timeLocal()),
                () -> assertEquals("GET /downloads/product_1 HTTP/1.1", logRecord.request()),
                () -> assertEquals(304, logRecord.status()),
                () -> assertEquals(0, logRecord.bodyBytesSent()),
                () -> assertEquals("-", logRecord.httpReferer()),
                () -> assertEquals("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)", logRecord.httpUserAgent()),
                () -> assertEquals("/downloads/product_1", logRecord.resource()),
                () -> assertEquals("GET", logRecord.method())
            );
        });
    }

    @Test
    @DisplayName("NginxLogParser Test with invalid log string")
    public void NginxLogParserTestInvalidLog() {
        NginxLogParser parser = new NginxLogParser();
        String logLine = "\"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" " +
            "\"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parser.parse(logLine, "log.txt");
        }, "Invalid log: " + logLine);
    }
}
