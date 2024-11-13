package backend.academy.services;

import backend.academy.records.LogRecord;
import java.time.ZonedDateTime;
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

    public Stream<String> filterByValue(String filterValue, Stream<String> lines) {
        Pattern pattern = Pattern.compile(filterValue);
        return lines
            .filter(line -> pattern.matcher(line).matches());
    }
}
