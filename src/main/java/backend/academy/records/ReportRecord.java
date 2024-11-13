package backend.academy.records;

import java.lang.reflect.Method;
import java.util.Map;

public record ReportRecord(
    long requestsCount,
    double averageResponseSize,
    double PercentileResponseSize,
    Map<String, Long> requestedResources,
    Map<Integer, Integer> commonResponseCodes,
    Long uniqueIpCount,
    Map<String, Long> methodRequestsCount

) {
}
