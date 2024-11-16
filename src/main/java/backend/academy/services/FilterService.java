package backend.academy.services;

import backend.academy.enums.FilterField;
import backend.academy.records.LogRecord;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FilterService {
    public Stream<LogRecord> filterByStartTime(
        ZonedDateTime startTime,
        Stream<LogRecord> logRecords
    ) {
        return logRecords
            .filter(logRecord ->
                logRecord.timeLocal().isAfter(startTime)
            );
    }

    public Stream<LogRecord> filterByEndTime(
        ZonedDateTime endTime,
        Stream<LogRecord> logRecords
    ) {
        return logRecords
            .filter(logRecord ->
                logRecord.timeLocal().isBefore(endTime)
            );
    }

    public Stream<LogRecord> filterByValue(
        String filterValue,
        FilterField filterField,
        Stream<LogRecord> logRecordStream
    ) {
        Function<LogRecord, String> logFieldExtractor = getLogFieldExtractor(filterField);

        Pattern pattern = Pattern.compile(filterValue);
        return logRecordStream
            .filter(log -> pattern.matcher(logFieldExtractor.apply(log)).matches());
    }

    private Function<LogRecord, String> getLogFieldExtractor(FilterField filterField) {
        return switch (filterField) {
            case ADDRESS -> LogRecord::remoteAddr;
            case USER -> LogRecord::remoteUser;
            case REFERER -> LogRecord::httpReferer;
            case AGENT -> LogRecord::httpUserAgent;
            case METHOD -> LogRecord::method;
        };
    }
}
