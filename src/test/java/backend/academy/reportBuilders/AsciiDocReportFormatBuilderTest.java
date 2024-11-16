package backend.academy.reportBuilders;

import backend.academy.reportBuilders.implementations.AsciiDocReportFormatBuilder;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AsciiDocReportFormatBuilderTest {

    private AsciiDocReportFormatBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new AsciiDocReportFormatBuilder();
    }

    @DisplayName("Add filenames test")
    @Test
    public void addFilenamesTest() {
        Set<String> filenames = new LinkedHashSet<>();
        filenames.add("debug.log");
        filenames.add("error.log");
        filenames.add("access.log");
        builder.addFilenames(filenames);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Файл(-ы) | `debug.log`, `error.log`, `access.log`\n")
            .contains("|===\n");
    }

    @DisplayName("Add start date test")
    @Test
    public void addStartDateTest() {
        builder.addStartDate("2023-10-01");

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Начальная дата | 2023-10-01\n")
            .contains("|===\n");
    }

    @DisplayName("Add end date test")
    @Test
    public void addEndDateTest() {
        builder.addEndDate("2023-10-31");

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Конечная дата | 2023-10-31\n")
            .contains("|===\n");
    }

    @DisplayName("Add requests count test")
    @Test
    public void addRequestsCountTest() {
        builder.addRequestsCount(100);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Количество запросов | 100\n")
            .contains("|===\n");
    }

    @DisplayName("Add unique IPs count test")
    @Test
    public void addUniqueIPsCountTest() {
        builder.addUniqueIpsCount(50);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Количество уникальных IP | 50\n")
            .contains("|===\n");
    }

    @DisplayName("Add average response size test")
    @Test
    public void addAverageResponseSizeTest() {
        builder.addAverageResponsesSize(1024.5);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| Средний размер ответа | 1024.5b\n")
            .contains("|===\n");
    }

    @DisplayName("Add response percentile test")
    @Test
    public void addResponsePercentileTest() {
        builder.addResponsePercentile(2048.0);
        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("| 95p размера ответа | 2048.0b\n")
            .contains("|===\n");
    }

    @DisplayName("Add requested resources statistics test")
    @Test
    public void addRequestedResourcesTest() {
        Map<String, Long> statistics = Map.of(
            "/resource1", 10L,
            "/resource2", 20L
        );
        builder.addRequestedResourcesStatistics(statistics);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("|===\n")
            .contains("\n== Запрашиваемые ресурсы\n\n")
            .contains("|===\n")
            .contains("| **Ресурс** | **Количество**\n")
            .contains("| /resource1 | 10\n")
            .contains("| /resource2 | 20\n")
            .contains("|===\n");
    }

    @DisplayName("Add responses codes statistics test")
    @Test
    public void addResponsesCodesStatistics() {
        Map<Integer, Long> statistics = Map.of(
            200, 50L,
            404, 10L
        );
        builder.addResponsesCodesStatistics(statistics);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("|===\n")
            .contains("\n== Коды ответа\n\n")
            .contains("|===\n")
            .contains("| **Код** | **Имя** | **Количество**\n")
            .contains("| 200 | OK | 50\n")
            .contains("| 404 | Not Found | 10\n")
            .contains("|===\n");
    }

    @DisplayName("Add requests methods statistics test")
    @Test
    public void addRequestsMethodsStatisticsTest() {
        Map<String, Long> statistics = Map.of(
            "GET", 30L,
            "POST", 20L
        );
        builder.addRequestsMethodsStatistics(statistics);

        String report = builder.buildReport();

        assertThat(report)
            .contains("== Общие сведения\n\n")
            .contains("|===\n")
            .contains("| **Метрика** | **Значение**\n")
            .contains("|===\n")
            .contains("\n== Методы запросов\n\n")
            .contains("|===\n")
            .contains("| **Метод** | **Количество**\n")
            .contains("| GET | 30\n")
            .contains("| POST | 20\n")
            .contains("|===\n");
    }
}
