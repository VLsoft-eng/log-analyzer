package backend.academy.jcommander;

import backend.academy.enums.ReportFormat;
import com.beust.jcommander.Parameter;
import java.time.ZonedDateTime;
import java.util.Optional;

public class Args {
    @Parameter(names = "--path", description = "Path to one or more NGINX log files (local template or URL)", required = true)
    public String path;

    @Parameter(names = "--format", converter = ReportFormatConverter.class, description = "Logs report output format (markdown или adoc)")
    public ReportFormat format = ReportFormat.MARKDOWN;

    @Parameter(names = "--from", converter = ISO8601Converter.class, description = "Specifying the start date for filtering logs")
    public ZonedDateTime from;

    @Parameter(names = "--to", converter = ISO8601Converter.class, description = "Specifying the end date for filtering logs")
    public ZonedDateTime to;

    @Parameter(names = "--filter-value", description = "Filter value for filtering logs")
    public String filterValue;

    public Optional<ZonedDateTime> getFrom() {
        return Optional.ofNullable(from);
    }

    public Optional<ZonedDateTime> getTo() {
        return Optional.ofNullable(to);
    }

    public Optional<String> getFilterValue() {
        return Optional.ofNullable(filterValue);
    }
}
