package backend.academy.jcommander;

import backend.academy.enums.ReportFormat;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class ReportFormatConverter implements IStringConverter<ReportFormat> {
    @Override
    public ReportFormat convert(String value) {
        try {
            return ReportFormat.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParameterException("Invalid report format: " + value,e);
        }
    }
}
