package backend.academy.jcommander;

import backend.academy.enums.FilterField;
import backend.academy.enums.ReportFormat;
import com.beust.jcommander.Parameter;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.Getter;

@Getter
public class Args {
    @Parameter(names = "--path", description = "Path to NGINX logs (local template or URL)", required = true)
    public String path;

    @Parameter(names = "--format", converter = ReportFormatConverter.class,
        description = "Logs report output format (markdown или adoc)")
    public ReportFormat format = ReportFormat.MARKDOWN;

    @Parameter(names = "--from", converter = ISO8601Converter.class, description = "Start date for filtering")
    public ZonedDateTime from;

    @Parameter(names = "--to", converter = ISO8601Converter.class, description = "End date for filtering")
    public ZonedDateTime to;

    @Parameter(names = "--filter-value", description = "Filter value for filtering logs")
    public String filterValue;

    @Parameter(names = "--filter-field", converter = FilterFieldConverter.class,
        description = "Filter field for filtering")
    public FilterField filterField;

    public Optional<ZonedDateTime> getFrom() {
        return Optional.ofNullable(from);
    }

    public Optional<ZonedDateTime> getTo() {
        return Optional.ofNullable(to);
    }

    public Optional<String> getFilterValue() {
        return Optional.ofNullable(filterValue);
    }

    public Optional<FilterField> getFilterField() {
        return Optional.ofNullable(filterField);
    }
}
