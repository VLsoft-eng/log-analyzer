package backend.academy;

import backend.academy.IO.OutputManager;
import backend.academy.enums.FilterField;
import backend.academy.jcommander.Args;
import backend.academy.records.LineRecord;
import backend.academy.records.LogRecord;
import backend.academy.records.ReportFormatOptions;
import backend.academy.records.ReportRecord;
import backend.academy.services.AnalyticService;
import backend.academy.services.FilterService;
import backend.academy.services.ReportFormatService;
import backend.academy.utilities.logReaders.factory.LogReaderChainFactory;
import backend.academy.utilities.logReaders.abstractions.LogReader;
import backend.academy.utilities.parsers.NginxLogParser;
import java.io.PrintStream;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public class LogProcessingManager {
    private final NginxLogParser parser;
    private final LogReader reader;
    private final FilterService filterService;
    private final AnalyticService analyticService;
    private final ReportFormatService reportFormatService;
    private final OutputManager outputManager;

    public LogProcessingManager(PrintStream out) {
        parser = new NginxLogParser();
        reader = LogReaderChainFactory.getLogReaderChain();
        filterService = new FilterService();
        analyticService = new AnalyticService();
        reportFormatService = new ReportFormatService();
        outputManager = new OutputManager(out);
    }

    public void process(Args args) {
        Optional<Stream<LineRecord>> stringStream = reader.getLogLines(args.path());
        if (stringStream.isEmpty()) {
            System.err.println("No log file found");
            return;
        }

        Stream<LogRecord> logRecordStream = stringStream.orElseThrow()
            .map(lineRecord -> parser.parse(lineRecord.value(), lineRecord.resourceName()));

        if (args.getFilterField().isPresent() && args.getFilterValue().isPresent()) {
            String filterValue = args.getFilterValue().orElseThrow();
            FilterField filterField = args.getFilterField().orElseThrow();
            logRecordStream = filterService.filterByValue(filterValue, filterField, logRecordStream);
        }

        if (args.getFrom().isPresent()) {
            ZonedDateTime from = args.getFrom().orElseThrow();
            logRecordStream = filterService.filterByStartTime(from, logRecordStream);
        }

        if (args.getTo().isPresent()) {
            ZonedDateTime to = args.getTo().orElseThrow();
            logRecordStream = filterService.filterByEndTime(to, logRecordStream);
        }

        ReportRecord reportRecord = analyticService.getLogReport(logRecordStream);

        ReportFormatOptions reportFormatOptions = new ReportFormatOptions(
            reportRecord,
            args.getFrom(),
            args.getTo(),
            args.format()
        );
        String formattedReportLine = reportFormatService.getReportString(reportFormatOptions);

        outputManager.print(formattedReportLine);
    }
}
