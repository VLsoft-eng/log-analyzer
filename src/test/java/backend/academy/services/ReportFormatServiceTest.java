package backend.academy.services;

import backend.academy.enums.ReportFormat;
import backend.academy.records.ReportFormatOptions;
import backend.academy.records.ReportRecord;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReportFormatServiceTest {

    private static ReportFormatService reportFormatService;

    @BeforeAll static void setUp() {
        reportFormatService = new ReportFormatService();
    }

    @Test
    void testGetReportStringWithMarkdownFormat() {
        ReportFormatOptions options = getTestOptions(ReportFormat.MARKDOWN);

        String report = reportFormatService.getReportString(options);

        assertThat(report).isNotBlank();
    }

    @Test
    void testGetReportStringWithAsciiDocFormat() {
        ReportFormatOptions options = getTestOptions(ReportFormat.ADOC);

        String report = reportFormatService.getReportString(options);

        assertThat(report).isNotBlank();
    }

    private ReportFormatOptions getTestOptions(ReportFormat reportFormat) {
        ReportRecord reportRecord = new ReportRecord(
            Set.of("access.log", "error.log"),
            1500,
            524.4,
            1024.0,
            Map.of("/home", 500L, "/api/data", 700L),
            Map.of(200, 1300L, 404, 150L, 500, 50L),
            300,
            Map.of("GET", 1200L, "POST", 300L)
        );

        return new ReportFormatOptions(
            reportRecord,
            Optional.of(ZonedDateTime.parse("2024-01-01T00:00:00Z")),
            Optional.of(ZonedDateTime.parse("2024-12-31T23:59:59Z")),
            reportFormat
        );
    }
}
