package backend.academy.services;

import backend.academy.enums.ReportFormat;
import backend.academy.records.ReportFormatOptions;
import backend.academy.records.ReportRecord;
import backend.academy.utilities.reportBuilders.abstractions.ReportFormatBuilder;
import backend.academy.utilities.reportBuilders.implementations.AsciiDocReportFormatBuilder;
import backend.academy.utilities.reportBuilders.implementations.MarkdownReportFormatBuilder;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

public class ReportFormatService {
    private static final Map<ReportFormat, ReportFormatBuilder> FORMAT_BUILDER_MAP = Map.of(
        ReportFormat.MARKDOWN, new MarkdownReportFormatBuilder(),
        ReportFormat.ADOC, new AsciiDocReportFormatBuilder()
    );

    public String getReportString(ReportFormatOptions reportFormatOptions) {
        ReportRecord reportRecord = reportFormatOptions.reportRecord();
        Optional<ZonedDateTime> startDate = reportFormatOptions.startDate();
        Optional<ZonedDateTime> endDate = reportFormatOptions.endDate();

        ReportFormatBuilder builder = FORMAT_BUILDER_MAP.get(reportFormatOptions.format());

        builder.addRequestsCount(reportRecord.requestsCount());
        builder.addFilenames(reportRecord.filenames());
        builder.addUniqueIpsCount(reportRecord.uniqueIpCount());
        builder.addAverageResponsesSize(reportRecord.averageResponseSize());
        builder.addRequestedResourcesStatistics(reportRecord.requestedResources());
        builder.addRequestsMethodsStatistics(reportRecord.requestMethodsStatistics());
        builder.addResponsesCodesStatistics(reportRecord.responseCodesStatistics());
        builder.addResponsePercentile(reportRecord.percentileResponseSize());
        builder.addStartDate(startDate.map(ZonedDateTime::toString).orElse("-"));
        builder.addEndDate(endDate.map(ZonedDateTime::toString).orElse("-"));

        return builder.buildReport();
    }

}
