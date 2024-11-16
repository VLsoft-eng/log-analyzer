package backend.academy.utilities.statisticsUtilities;

import backend.academy.records.LogRecord;
import com.datadoghq.sketch.ddsketch.DDSketch;
import com.google.zetasketch.HyperLogLogPlusPlus;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

public class LogStatisticsAccumulator {
    private static final double PERCENTILE_ACCURACY = 0.01;
    private static final double QUANTILE = 0.95;
    private static final int NORMAL_PRECISION = 13;
    private static final int SPARSE_PRECISION = 14;
    private static final int RESOURCES_LIMIT = 3;
    private static final int RESPONSE_CODES_LIMIT = 3;
    private static final int REQUESTS_METHODS_LIMIT = 3;

    @Getter private long requestsCount;
    @Getter private final Set<String> filenames;
    private double totalResponsesSize;
    private final Map<String, Long> requestedResourcesCount;
    private final Map<Integer, Long> responseCodesCount;
    private final Map<String, Long> methodRequestsCount;
    private final DDSketch percentileSketch;
    private final HyperLogLogPlusPlus<String> uniqueIpsHll;

    public LogStatisticsAccumulator() {
        requestsCount = 0;
        totalResponsesSize = 0;
        requestedResourcesCount = new HashMap<>();
        responseCodesCount = new HashMap<>();
        methodRequestsCount = new HashMap<>();
        uniqueIpsHll = new HyperLogLogPlusPlus.Builder()
            .normalPrecision(NORMAL_PRECISION).sparsePrecision(SPARSE_PRECISION)
            .buildForStrings();
        percentileSketch = new DDSketch(PERCENTILE_ACCURACY);
        filenames = new HashSet<>();
    }

    public void accumulate(LogRecord logRecord) {
        requestsCount++;
        totalResponsesSize += logRecord.bodyBytesSent();
        requestedResourcesCount.merge(logRecord.resource(), 1L, Long::sum);
        responseCodesCount.merge(logRecord.status(), 1L, Long::sum);
        methodRequestsCount.merge(logRecord.method(), 1L, Long::sum);
        uniqueIpsHll.add(logRecord.remoteAddr());
        percentileSketch.accept(logRecord.bodyBytesSent());
        filenames.add(logRecord.fileName());
    }

    public void combine(LogStatisticsAccumulator accumulator) {
        requestsCount += accumulator.requestsCount;
        totalResponsesSize += accumulator.totalResponsesSize;
        requestedResourcesCount.putAll(accumulator.requestedResourcesCount);
        responseCodesCount.putAll(accumulator.responseCodesCount);
        methodRequestsCount.putAll(accumulator.methodRequestsCount);
        filenames.addAll(accumulator.filenames);
        percentileSketch.mergeWith(percentileSketch);
        uniqueIpsHll.merge(accumulator.uniqueIpsHll);
    }

    public double getPercentile() {
        return !percentileSketch.isEmpty() ? percentileSketch.getValueAtQuantile(QUANTILE) : 0.0;
    }

    public long getUniqueIpsCount() {
        return uniqueIpsHll.result();
    }

    public Map<String, Long> getRequestResourcesStatistics() {
        return requestedResourcesCount
            .entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(RESOURCES_LIMIT)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (firstValue, secondValue) -> firstValue,
                LinkedHashMap::new
            ));
    }

    public Map<Integer, Long> getResponseCodesStatistics() {
        return responseCodesCount
            .entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(RESPONSE_CODES_LIMIT)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (firstValue, secondValue) -> firstValue,
                LinkedHashMap::new
            ));
    }

    public Map<String, Long> getRequestMethodsStatistics() {
        return methodRequestsCount
            .entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .limit(REQUESTS_METHODS_LIMIT)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (fistValue, secondValue) -> fistValue,
                LinkedHashMap::new
            ));
    }

    public double getAverageResponsesSize() {
        return !(requestsCount == 0) ? totalResponsesSize / requestsCount : 0.0;
    }
}
