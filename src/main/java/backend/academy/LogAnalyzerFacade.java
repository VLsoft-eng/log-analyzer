package backend.academy;

import backend.academy.IO.OutputManager;
import backend.academy.exceptions.LogLinesNotFoundException;
import backend.academy.jcommander.Args;
import backend.academy.records.LogRecord;
import backend.academy.records.ReportFormatOptions;
import backend.academy.records.ReportRecord;
import backend.academy.services.LogProcessingService;
import backend.academy.services.LogRetrievingService;
import backend.academy.services.ReportFormatService;
import java.io.PrintStream;
import java.util.stream.Stream;

public class LogAnalyzerFacade {
    private final LogRetrievingService logRetrievingService;
    private final LogProcessingService logProcessingService;
    private final ReportFormatService reportFormatService;
    private final OutputManager outputManager;

    public LogAnalyzerFacade(PrintStream out) {
        logRetrievingService = new LogRetrievingService();
        logProcessingService = new LogProcessingService();
        reportFormatService = new ReportFormatService();
        outputManager = new OutputManager(out);
    }

    public void analyze(Args args) {
        try {
            Stream<LogRecord> logRecordStream = logRetrievingService.retrieveLogs(args);
            ReportRecord reportRecord = logProcessingService.processLogsToReport(logRecordStream, args);
            ReportFormatOptions reportFormatOptions = new ReportFormatOptions(
                reportRecord,
                args.getFrom(),
                args.getTo(),
                args.format()
            );
            String formattedReportLine = reportFormatService.getReportString(reportFormatOptions);
            outputManager.print(formattedReportLine);
        } catch (LogLinesNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
