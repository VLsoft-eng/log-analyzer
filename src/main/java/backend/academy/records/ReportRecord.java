package backend.academy.records;

import java.util.Map;
import java.util.Set;

public record ReportRecord(
    Set<String> filenames,
    long requestsCount,
    double averageResponseSize,
    double percentileResponseSize,
    Map<String, Long> requestedResources,
    Map<Integer, Long> responseCodesStatistics,
    long uniqueIpCount,
    Map<String, Long> requestMethodsStatistics
) {
}
