package backend.academy.services;

import backend.academy.enums.FilterField;
import backend.academy.jcommander.Args;
import backend.academy.records.LogRecord;
import backend.academy.records.ReportRecord;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class LogProcessingService {
    private final FilterService filterService;
    private final AnalyticService analyticService;

    public LogProcessingService() {
        this.filterService = new FilterService();
        this.analyticService = new AnalyticService();
    }

    public ReportRecord processLogsToReport(Stream<LogRecord> logRecordStream, Args args) {
        Stream<LogRecord> filteredStream = logRecordStream;

        if (args.getFilterField().isPresent() && args.getFilterValue().isPresent()) {
            String filterValue = args.getFilterValue().orElseThrow();
            FilterField filterField = args.getFilterField().orElseThrow();
            filteredStream = filterService.filterByValue(filterValue, filterField, logRecordStream);
        }

        if (args.getFrom().isPresent()) {
            ZonedDateTime from = args.getFrom().orElseThrow();
            filteredStream = filterService.filterByStartTime(from, logRecordStream);
        }

        if (args.getTo().isPresent()) {
            ZonedDateTime to = args.getTo().orElseThrow();
            filteredStream = filterService.filterByEndTime(to, logRecordStream);
        }

        return analyticService.getLogReport(filteredStream);
    }
}
