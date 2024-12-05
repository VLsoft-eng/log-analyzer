package backend.academy.services;

import backend.academy.exceptions.LogLinesNotFoundException;
import backend.academy.jcommander.Args;
import backend.academy.records.LogRecord;
import backend.academy.utilities.logReaders.abstractions.LogReader;
import backend.academy.utilities.logReaders.factory.LogReaderChainFactory;
import backend.academy.utilities.parsers.NginxLogParser;
import java.util.stream.Stream;

public class LogRetrievingService {
    private final NginxLogParser parser;
    private final LogReader reader;

    public LogRetrievingService() {
        parser = new NginxLogParser();
        reader = LogReaderChainFactory.getLogReaderChain();
    }

    public Stream<LogRecord> retrieveLogs(Args args) {
        return reader.getLogLines(args.path())
            .orElseThrow(LogLinesNotFoundException::new)
            .map(lineRecord -> parser.parse(lineRecord.value(), lineRecord.resourceName()));
    }
}
