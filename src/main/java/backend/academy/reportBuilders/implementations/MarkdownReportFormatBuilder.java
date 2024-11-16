package backend.academy.reportBuilders.implementations;

import backend.academy.reportBuilders.abstractions.AbstractReportFormatBuilder;
import backend.academy.reportBuilders.abstractions.ReportFormatBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MarkdownReportFormatBuilder extends AbstractReportFormatBuilder implements ReportFormatBuilder {
    private static final String COMMON_TABLE_HEADER =
        "| **Метрика** | **Значение** |\n|:---------------------:|-------------:|\n";
    private static final String RESOURCE_TABLE_HEADER =
        "| **Ресурс** | **Количество** |\n|:---------------:|-----------:|\n";
    private static final String RESPONSE_CODE_TABLE_HEADER =
        "| **Код** | **Имя** | **Количество** |\n|:---:|:---------------------:|-----------:|\n";
    private static final String REQUEST_METHOD_TABLE_HEADER =
        "| **Метод** | **Количество** |\n|:---:|-----------:|\n";
    private static final String PIPE = " | ";
    private static final String PIPE_NEWLINE = " |\n";

    private final StringBuilder stringBuilder = new StringBuilder();
    private final Map<String, String> metrics = new LinkedHashMap<>();
    private final List<String> requestedResources = new ArrayList<>();
    private final List<String> responseCodes = new ArrayList<>();
    private final List<String> requestMethods = new ArrayList<>();

    @Override
    public void addFilenames(Set<String> filenames) {
        String formattedFilenames = filenames.stream()
            .map(filename -> "`" + filename + "`")
            .collect(Collectors.joining(", "));
        metrics.put("Файл(-ы)", formattedFilenames);
    }

    @Override
    public void addStartDate(String startDate) {
        metrics.put("Начальная дата", startDate != null ? startDate : "-");
    }

    @Override
    public void addEndDate(String endDate) {
        metrics.put("Конечная дата", endDate != null ? endDate : "-");
    }

    @Override
    public void addRequestsCount(int requestsCount) {
        metrics.put("Количество запросов", String.valueOf(requestsCount));
    }

    @Override
    public void addUniqueIpsCount(long uniqueIpsCount) {
        metrics.put("Количество уникальных IP", String.valueOf(uniqueIpsCount));
    }

    @Override
    public void addAverageResponsesSize(Double averageResponsesSize) {
        metrics.put("Средний размер ответа", averageResponsesSize + "b");
    }

    @Override
    public void addResponsePercentile(Double responsePercentile) {
        metrics.put("95p размера ответа", responsePercentile + "b");
    }

    @Override
    public void addRequestedResourcesStatistics(Map<String, Long> statistics) {
        requestedResources.add(RESOURCE_TABLE_HEADER);
        statistics.forEach((resource, count) ->
            requestedResources.add("| " + resource + PIPE + count + PIPE_NEWLINE));
    }

    @Override
    public void addResponsesCodesStatistics(Map<Integer, Long> statistics) {
        responseCodes.add(RESPONSE_CODE_TABLE_HEADER);
        statistics.forEach((code, count) ->
            responseCodes.add("| " + code + PIPE + HTTP_STATUS_DESCRIPTIONS.get(code) + PIPE + count + PIPE_NEWLINE));
    }

    @Override
    public void addRequestsMethodsStatistics(Map<String, Long> statistics) {
        requestMethods.add(REQUEST_METHOD_TABLE_HEADER);
        statistics.forEach((method, count) ->
            requestMethods.add("| " + method + PIPE + count + PIPE_NEWLINE));
    }

    @Override
    public String buildReport() {
        stringBuilder.append("### Общие сведения\n\n");
        stringBuilder.append(COMMON_TABLE_HEADER);
        metrics.forEach(
            (key, value) -> stringBuilder.append("| **")
                .append(key).append("** | ")
                .append(value)
                .append(PIPE_NEWLINE));

        appendSection("Запрашиваемые ресурсы", requestedResources);
        appendSection("Коды ответа", responseCodes);
        appendSection("Методы запросов", requestMethods);

        return stringBuilder.toString();
    }

    private void appendSection(String title, List<String> content) {
        if (!content.isEmpty()) {
            stringBuilder.append("\n#### ").append(title).append("\n\n");
            content.forEach(stringBuilder::append);
            stringBuilder.append("\n");
        }
    }
}
