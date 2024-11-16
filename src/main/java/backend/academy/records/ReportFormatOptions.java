package backend.academy.records;

import backend.academy.enums.ReportFormat;
import java.time.ZonedDateTime;
import java.util.Optional;

public record ReportFormatOptions(
    ReportRecord reportRecord,
    Optional<ZonedDateTime> startDate,
    Optional<ZonedDateTime> endDate,
    ReportFormat format
) {
}
