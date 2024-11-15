package backend.academy.logReaders.abstractions;

import backend.academy.records.LineRecord;
import java.util.Optional;
import java.util.stream.Stream;

public interface LogReader {
    Optional<Stream<LineRecord>> getLogLines(String path);

    void setNextLogReader(LogReader reader);
}
