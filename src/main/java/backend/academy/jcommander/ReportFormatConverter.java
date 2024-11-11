package backend.academy.jcommander;

import backend.academy.enums.ReportFormat;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class ReportFormatConverter implements IStringConverter<ReportFormat> {
    @Override
    public ReportFormat convert(String value) {
        if (value != null) {
            return switch (value.toUpperCase()) {
                case "MARKDOWN" -> ReportFormat.MARKDOWN;
                case "ADOC" -> ReportFormat.ADOC;
                default ->
                    throw new ParameterException("Invalid format: " + value + ". Valid options are MARKDOWN or ADOC.");
            };
        }
        return ReportFormat.MARKDOWN;
    }
}
