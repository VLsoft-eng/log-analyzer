package backend.academy.LogReader;

import backend.academy.logReaders.LogReaderChainFactory;
import backend.academy.logReaders.abstractions.LogReader;
import backend.academy.logReaders.implementations.LocalLogReader;
import backend.academy.logReaders.implementations.UrlLogReader;
import backend.academy.records.LineRecord;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogReaderTest {

    @DisplayName("Local Log Reader test with valid glob pattern")
    @Test
    public void localLogReaderValidPathTest() {
        LogReader reader = new LocalLogReader();
        String path = "*/**/logs/*";

        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(path);

        assertTrue(stringStream.isPresent());
        assertTrue(stringStream.orElseThrow().findAny().isPresent());
    }

    @DisplayName("Url Log Reader Test with valid url")
    @Test
    public void urlLogReaderValidPathTest() {
        LogReader reader = new UrlLogReader();
        String path = "https://raw.githubusercontent.com/" +
            "elastic/examples/master/Common%20Data%20Formats/" +
            "nginx_logs/nginx_logs";

        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(path);

        assertTrue(stringStream.isPresent());
        assertTrue(stringStream.orElseThrow().findAny().isPresent());
    }

    @DisplayName("Url Log Reader Test with invalid url")
    @Test
    public void urlLogReaderInvalidPathTest() {
        LogReader reader = new UrlLogReader();
        String path = "https://raw.invalid.com/" +
            "elastic/examples/master/Common%20Data%20Formats/" +
            "nginx_logs/nginx_logs";

        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(path);

        assertTrue(stringStream.isEmpty());
    }

    @DisplayName("Log reader chain test when path is url")
    @Test
    public void logReaderChainUrlPathTest() {
        LogReader reader = LogReaderChainFactory.getLogReaderChain();
        String path = "https://raw.githubusercontent.com/" +
            "elastic/examples/master/Common%20Data%20Formats/" +
            "nginx_logs/nginx_logs";

        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(path);

        assertTrue(stringStream.isPresent());
        assertTrue(stringStream.orElseThrow().findAny().isPresent());
    }

    @DisplayName("Log reader chain test when path is glob pattern")
    @Test
    public void logReaderChainGlobPathTest() {
        LogReader reader = LogReaderChainFactory.getLogReaderChain();
        String path = "*/**/logs/*";

        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(path);

        assertTrue(stringStream.isPresent());
        assertTrue(stringStream.orElseThrow().findAny().isPresent());
    }
}
