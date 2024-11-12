package backend.academy.jcommander;

import backend.academy.enums.ReportFormat;
import com.beust.jcommander.JCommander;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgsTest {
    @DisplayName("Test parsing args in args class for jcommander")
    @Test
    public void ArgsParsingTest() {
        String[] args = new String[] {
            "--path", "logs/*.log",
            "--format", "markdown",
            "--from", "2024-11-01T10:00:00",
            "--to", "2024-11-10T10:00:00",
            "--filter-value", "404"
        };

        Args argsObj = new Args();
        JCommander.newBuilder()
            .addObject(argsObj)
            .build()
            .parse(args);

        assertEquals("logs/*.log", argsObj.path);
        assertEquals(ReportFormat.MARKDOWN, argsObj.format);
        assertEquals(ZonedDateTime.parse("2024-11-01T10:00:00+00:00", DateTimeFormatter.ISO_ZONED_DATE_TIME),
            argsObj.from);
        assertEquals(ZonedDateTime.parse("2024-11-10T10:00:00+00:00", DateTimeFormatter.ISO_ZONED_DATE_TIME),
            argsObj.to);
        assertEquals("404", argsObj.filterValue);
    }

    @DisplayName("Args optional fields test. Must be empty if not provided.")
    @Test
    public void ArgsOptionalFieldsTest() {
        String[] args = new String[] {
            "--path", "logs/*.log"
        };

        Args argsObj = new Args();
        JCommander.newBuilder()
            .addObject(argsObj)
            .build()
            .parse(args);

        assertTrue(argsObj.getFrom().isEmpty());
        assertTrue(argsObj.getTo().isEmpty());
        assertTrue(argsObj.getFilterValue().isEmpty());
    }

}
