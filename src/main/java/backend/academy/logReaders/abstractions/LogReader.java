package backend.academy.logReaders.abstractions;

import java.util.Optional;
import java.util.stream.Stream;

public interface LogReader {
    Optional<Stream<String>> getLogLines(String path);

    void setNextLogReader(LogReader reader);
}
