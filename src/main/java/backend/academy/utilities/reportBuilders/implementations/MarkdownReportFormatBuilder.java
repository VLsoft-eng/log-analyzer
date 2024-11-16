package backend.academy.utilities.reportBuilders.implementations;

import backend.academy.utilities.reportBuilders.abstractions.AbstractReportFormatBuilder;
import backend.academy.utilities.reportBuilders.abstractions.ReportFormatBuilder;
import java.util.List;
import java.util.Map;

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

        appendSection(MOST_REQUESTED_RESOURCES, requestedResources);
        appendSection(MOST_RESPONSES_CODES, responseCodes);
        appendSection(MOST_REQUESTS_METHODS, requestMethods);

        return stringBuilder.toString();
    }

    private void appendSection(String title, List<String> content) {
        if (!content.isEmpty()) {
            stringBuilder.append("\n#### ").append(title).append("\n\n");
            content.forEach(stringBuilder::append);
            stringBuilder.append('\n');
        }
    }
}
