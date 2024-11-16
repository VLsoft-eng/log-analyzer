package backend.academy.reportBuilders.abstractions;

import java.util.Map;
import java.util.Set;

public interface ReportFormatBuilder {
    void addFilenames(Set<String> filenames);

    void addStartDate(String startDate);

    void addEndDate(String endDate);

    void addRequestsCount(long requestsCount);

    void addUniqueIpsCount(long responsesCount);

    void addAverageResponsesSize(Double averageResponsesSize);

    void addResponsePercentile(Double responsePercentile);

    void addRequestedResourcesStatistics(Map<String, Long> statistics);

    void addResponsesCodesStatistics(Map<Integer, Long> statistics);

    void addRequestsMethodsStatistics(Map<String, Long> statistics);

    String buildReport();
}
