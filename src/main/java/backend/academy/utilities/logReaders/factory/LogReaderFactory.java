package backend.academy.utilities.logReaders.factory;

import backend.academy.enums.LogReaderType;
import backend.academy.utilities.logReaders.abstractions.LogReader;
import backend.academy.utilities.logReaders.implementations.LocalLogReader;
import backend.academy.utilities.logReaders.implementations.UrlLogReader;
import java.util.Map;

public final class LogReaderFactory {

    private LogReaderFactory() {
    }

    private final static Map<LogReaderType, LogReader> LOG_READER_MAP = Map.of(
        LogReaderType.LOCAL, new LocalLogReader(),
        LogReaderType.URL, new UrlLogReader()
    );

    public static LogReader getLogReader(final LogReaderType type) {
        return LOG_READER_MAP.get(type);
    }

    public static LogReader getLogReader(final LogReaderType type, LogReader nextLogReader) {
        LogReader logReader = LOG_READER_MAP.get(type);
        logReader.setNextLogReader(nextLogReader);
        return logReader;
    }
}
