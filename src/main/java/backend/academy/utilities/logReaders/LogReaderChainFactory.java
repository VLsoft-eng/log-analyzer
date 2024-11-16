package backend.academy.utilities.logReaders;

import backend.academy.enums.LogReaderType;
import backend.academy.utilities.logReaders.abstractions.LogReader;
import java.util.List;

public final class LogReaderChainFactory {

    private LogReaderChainFactory() {
    }

    public static LogReader getLogReaderChain() {
        List<LogReaderType> logReaderTypes = List.of(LogReaderType.values());
        LogReader currentLogReader = null;
        for (LogReaderType logReaderType : logReaderTypes) {
            currentLogReader = LogReaderFactory.getLogReader(logReaderType, currentLogReader);
        }

        return currentLogReader;
    }
}
