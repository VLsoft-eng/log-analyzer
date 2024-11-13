package backend.academy.services;

import backend.academy.records.LogRecord;
import java.time.ZonedDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterServiceTest {
    private static FilterService filterService;

    @BeforeAll
    public static void setUp() {
        filterService = new FilterService();
    }

    @DisplayName("Filter by start time test")
    @Test
    public void filterByStartTimeTest() {
        ZonedDateTime startTime = ZonedDateTime.now().minusHours(1);
        LogRecord logRecord1 = mock(LogRecord.class);
        LogRecord logRecord2 = mock(LogRecord.class);
        when(logRecord1.timeLocal()).thenReturn(ZonedDateTime.now());
        when(logRecord2.timeLocal()).thenReturn(ZonedDateTime.now().minusHours(2));
        Stream<LogRecord> logRecords = Stream.of(logRecord1, logRecord2);

        Stream<LogRecord> filtered = filterService.filterByStartTime(startTime, logRecords);

        assertEquals(1, filtered.count());
    }

    @DisplayName("Filter by end time test")
    @Test
    public void filterByEndTimeTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        LogRecord logRecord1 = mock(LogRecord.class);
        LogRecord logRecord2 = mock(LogRecord.class);
        when(logRecord1.timeLocal()).thenReturn(ZonedDateTime.now().plusHours(2));
        when(logRecord2.timeLocal()).thenReturn(ZonedDateTime.now().minusHours(2));
        Stream<LogRecord> logRecords = Stream.of(logRecord1, logRecord2);

        Stream<LogRecord> filtered = filterService.filterByStartTime(startTime, logRecords);

        assertEquals(1, filtered.count());
    }

    @DisplayName("Filter by value test")
    @Test
    public void filterByValueTest() {
        String string1 = "test";
        String string2 = "deploy";
        String filterValue = "t.st";
        Stream<String> stringStream = Stream.of(string1, string2);

        Stream<String> filtered = filterService.filterByValue(filterValue, stringStream);

        assertEquals(1, filtered.count());
    }
}
