package backend.academy.reportBuilders;

import backend.academy.utilities.reportBuilders.implementations.MarkdownReportFormatBuilder;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MarkdownReportFormatBuilderTest {

    private MarkdownReportFormatBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new MarkdownReportFormatBuilder();
    }

    @DisplayName("Add filenames into builder test")
    @Test
    public void addFilenamesTest() {
        Set<String> filenames = new LinkedHashSet<>();
        filenames.add("debug.log");
        filenames.add("error.log");
        filenames.add("access.log");
        builder.addFilenames(filenames);

        String report = builder.buildReport();

        assertThat(report).contains("**Файл(-ы)** | `debug.log`, `error.log`, `access.log`");
    }

    @DisplayName("Add start date into builder test")
    @Test
    public void addStartDateTest() {
        builder.addStartDate("31.08.2024");

        String report = builder.buildReport();

        assertThat(report).contains("**Начальная дата** | 31.08.2024");
    }

    @DisplayName("Add end date into builder test")
    @Test
    public void addEndDateTest() {
        builder.addEndDate("01.09.2024");

        String report = builder.buildReport();

        assertThat(report).contains("**Конечная дата** | 01.09.2024");
    }

    @DisplayName("Add requests count into builder test")
    @Test
    public void addRequestsCountTest() {
        builder.addRequestsCount(10000);

        String report = builder.buildReport();

        assertThat(report).contains("**Количество запросов** | 10000");
    }

    @DisplayName("Full report generating test")
    @Test
    public void fullReportGeneratingTest() {
        Set<String> filenames = new LinkedHashSet<>();
        filenames.add("debug.log");
        filenames.add("error.log");
        filenames.add("access.log");
        Map<String, Long> resourcesStatistics = Map.of(
            "/index.html", 5000L,
            "/about.html", 2000L,
            "/contact.html", 1000L
        );
        Map<String, Long> requestMethodsStatistics = Map.of(
            "GET", 7000L,
            "POST", 2000L,
            "DELETE", 1000L
        );
        Map<Integer, Long> responseCodesStatistics = Map.of(
            200, 8000L,
            404, 1000L,
            500, 500L
        );
        builder.addFilenames(filenames);
        builder.addStartDate("31.08.2024");
        builder.addEndDate("01.09.2024");
        builder.addRequestsCount(10000);
        builder.addUniqueIpsCount(250L);
        builder.addAverageResponsesSize(1234.56);
        builder.addResponsePercentile(95.32);
        builder.addRequestedResourcesStatistics(resourcesStatistics);
        builder.addResponsesCodesStatistics(responseCodesStatistics);
        builder.addRequestsMethodsStatistics(requestMethodsStatistics);

        String report = builder.buildReport();

        assertThat(report)
            .contains("**Файл(-ы)** | `debug.log`, `error.log`, `access.log`")
            .contains("**Начальная дата** | 31.08.2024")
            .contains("**Конечная дата** | 01.09.2024")
            .contains("**Количество запросов** | 10000")
            .contains("**Количество уникальных IP** | 250")
            .contains("**Средний размер ответа** | 1234.56b")
            .contains("**95p размера ответа** | 95.32b")
            .contains("| /index.html | 5000 |")
            .contains("| /about.html | 2000 |")
            .contains("| /contact.html | 1000 |")
            .contains("| 200 | OK | 8000 |")
            .contains("| 404 | Not Found | 1000 |")
            .contains("| 500 | Internal Server Error | 500 |")
            .contains("| GET | 7000 |")
            .contains("| POST | 2000 |")
            .contains("| DELETE | 1000 |");
    }

    @DisplayName("Add unique Ips count into builder test")
    @Test
    public void addUniqueIpsCountTest() {
        builder.addUniqueIpsCount(250L);

        String report = builder.buildReport();

        assertThat(report).contains("**Количество уникальных IP** | 250");
    }

    @DisplayName("Add requested resources statistics into builder test")
    @Test
    public void addRequestedResourcesStatisticsTest() {
        Map<String, Long> resourcesStatistics = Map.of(
            "/index.html", 5000L,
            "/about.html", 2000L,
            "/contact.html", 1000L
        );
        builder.addRequestedResourcesStatistics(resourcesStatistics);

        String report = builder.buildReport();

        assertThat(report).contains("| **Ресурс** | **Количество** |")
            .contains("| /index.html | 5000 |")
            .contains("| /about.html | 2000 |")
            .contains("| /contact.html | 1000 |");
    }

    @DisplayName("Add responses codes statistics into builder test")
    @Test
    public void addResponsesCodesStatisticsTest() {
        Map<Integer, Long> responseCodesStatistics = Map.of(
            200, 8000L,
            404, 1000L,
            500, 500L
        );
        builder.addResponsesCodesStatistics(responseCodesStatistics);

        String report = builder.buildReport();

        assertThat(report).contains("| **Код** | **Имя** | **Количество** |")
            .contains("| 200 | OK | 8000 |")
            .contains("| 404 | Not Found | 1000 |")
            .contains("| 500 | Internal Server Error | 500 |");
    }

    @DisplayName("Add responses codes statistics into builder test")
    @Test
    public void addRequestsMethodsStatisticsTest() {
        Map<String, Long> requestMethodsStatistics = Map.of(
            "GET", 7000L,
            "POST", 2000L,
            "DELETE", 1000L
        );
        builder.addRequestsMethodsStatistics(requestMethodsStatistics);

        String report = builder.buildReport();

        assertThat(report).contains("| **Метод** | **Количество** |")
            .contains("| GET | 7000 |")
            .contains("| POST | 2000 |")
            .contains("| DELETE | 1000 |");
    }
}
