package backend.academy.jcommander;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;

public class ISO8601Converter implements IStringConverter<ZonedDateTime> {

    private static final DateTimeFormatter TS_FORMATTER =
        DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    @Override
    public ZonedDateTime convert(String value) {
        try {
            return ZonedDateTime
                .parse(value, TS_FORMATTER.withZone(ZoneOffset.UTC));
        } catch (DateTimeParseException e) {
            throw new ParameterException("Invalid timestamp. Expected format: uuuu-MM-dd'T'HH:mm:ss");
        }
    }
}
