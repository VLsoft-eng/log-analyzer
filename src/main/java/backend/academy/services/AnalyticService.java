package backend.academy.services;

import backend.academy.records.LogRecord;
import backend.academy.records.ReportRecord;
import backend.academy.statisticsUtilities.LogStatisticsAccumulator;
import java.util.stream.Stream;

public class AnalyticService {
    public ReportRecord getLogReport(Stream<LogRecord> logRecordStream) {
        LogStatisticsAccumulator result = logRecordStream
            .reduce(
                new LogStatisticsAccumulator(),
                (acc, logRecord) -> {
                    acc.accumulate(logRecord);
                    return acc;
                },
                (firstAcc, secondAcc) -> {
                    firstAcc.combine(secondAcc);
                    return firstAcc;
                }
            );

        return new ReportRecord(
            result.filenames(),
            result.requestsCount(),
            result.getAverageResponsesSize(),
            result.getPercentile(),
            result.getRequestResourcesStatistics(),
            result.getResponseCodesStatistics(),
            result.getUniqueIpsCount(),
            result.getRequestMethodsStatistics()
        );
    }
}
