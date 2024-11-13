package backend.academy.records;

import java.util.Map;

public record ReportRecord(
    long requestsCount,
    double averageResponseSize,
    double percentileResponseSize,
    Map<String, Long> requestedResources,
    Map<Integer, Integer> commonResponseCodes,
    Long uniqueIpCount,
    Map<String, Long> methodRequestsCount

) {
}
