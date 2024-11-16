package backend.academy.utilities.reportBuilders.implementations;

import backend.academy.utilities.reportBuilders.abstractions.AbstractReportFormatBuilder;
import backend.academy.utilities.reportBuilders.abstractions.ReportFormatBuilder;
import java.util.List;
import java.util.Map;

public class AsciiDocReportFormatBuilder extends AbstractReportFormatBuilder implements ReportFormatBuilder {
    private static final String COMMON_TABLE_HEADER =
        "|===\n| **Метрика** | **Значение**\n";
    private static final String RESOURCE_TABLE_HEADER =
        "|===\n| **Ресурс** | **Количество**\n";
    private static final String RESPONSE_CODE_TABLE_HEADER =
        "|===\n| **Код** | **Имя** | **Количество**\n";
    private static final String REQUEST_METHOD_TABLE_HEADER =
        "|===\n| **Метод** | **Количество**\n";

    private static final String PIPE_TABLE_SEPARATOR = "|===\n";
    private static final String PIPE = " | ";
    private static final String PIPE_WITHOUT_START_WHITESPACE = "| ";

    @Override
    public void addRequestedResourcesStatistics(Map<String, Long> statistics) {
        requestedResources.add(RESOURCE_TABLE_HEADER);
        statistics.forEach((resource, count) ->
            requestedResources.add(PIPE_WITHOUT_START_WHITESPACE + resource + PIPE + count + "\n"));
    }

    @Override
    public void addResponsesCodesStatistics(Map<Integer, Long> statistics) {
        responseCodes.add(RESPONSE_CODE_TABLE_HEADER);
        statistics.forEach((code, count) ->
            responseCodes.add(PIPE_WITHOUT_START_WHITESPACE + code + PIPE
                + HTTP_STATUS_DESCRIPTIONS.get(code) + PIPE + count + "\n"));
    }

    @Override
    public void addRequestsMethodsStatistics(Map<String, Long> statistics) {
        requestMethods.add(REQUEST_METHOD_TABLE_HEADER);
        statistics.forEach((method, count) ->
            requestMethods.add(PIPE_WITHOUT_START_WHITESPACE + method + PIPE + count + "\n"));
    }

    @Override
    public String buildReport() {
        stringBuilder.append("== Общие сведения\n\n");
        stringBuilder.append(COMMON_TABLE_HEADER);
        metrics.forEach(
            (key, value) -> stringBuilder.append(PIPE_WITHOUT_START_WHITESPACE)
                .append(key).append(PIPE)
                .append(value)
                .append('\n'));
        stringBuilder.append(RESPONSE_CODE_TABLE_HEADER);

        appendSection(stringBuilder, "Запрашиваемые ресурсы", requestedResources);
        appendSection(stringBuilder, "Коды ответа", responseCodes);
        appendSection(stringBuilder, "Методы запросов", requestMethods);

        return stringBuilder.toString();
    }

    private void appendSection(StringBuilder builder, String title, List<String> content) {
        if (!content.isEmpty()) {
            builder.append("\n== ").append(title).append("\n\n");
            content.forEach(builder::append);
            builder.append(PIPE_TABLE_SEPARATOR);
        }
    }
}
