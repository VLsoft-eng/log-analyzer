package backend.academy.statisticUtils;

import backend.academy.records.LogRecord;
import backend.academy.statisticsUtilities.LogStatisticsAccumulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogStatisticAccumulatorTest {
    private LogStatisticsAccumulator accumulator;

    @BeforeEach
    void setUp() {
        accumulator = new LogStatisticsAccumulator();
    }

    @DisplayName("Accumulate statistics test")
    @Test
    void accumulateStatisticsTest() {
        // Создаем поддельный LogRecord
        LogRecord logRecord = new LogRecord(
            "file", "127.0.0.1", "user", ZonedDateTime.now(),
            "GET /resource HTTP/1.1", 200, 1000, "http://referer", "Mozilla"
        );

        accumulator.accumulate(logRecord);

        assertEquals(1, accumulator.requestsCount());
        assertEquals(1000, accumulator.getAverageResponsesSize(), 0.001);
        assertTrue(accumulator.getRequestResourcesStatistics().containsKey("/resource"));
        assertTrue(accumulator.getResponseCodesStatistics().containsKey(200));
        assertTrue(accumulator.getRequestMethodsStatistics().containsKey("GET"));
        assertEquals(1, accumulator.getUniqueIpsCount());
        assertTrue(accumulator.filenames().contains("file"));
    }

    @DisplayName("Combine two log statistics accumulators test")
    @Test
    void combineStatisticsAccumulatorsTest() {
        LogStatisticsAccumulator accumulator1 = new LogStatisticsAccumulator();
        LogStatisticsAccumulator accumulator2 = new LogStatisticsAccumulator();
        LogRecord logRecord1 = new LogRecord(
            "file1", "127.0.0.1", "user1", ZonedDateTime.now(),
            "GET /resource1 HTTP/1.1", 200, 500, "http://referer1", "Mozilla"
        );
        LogRecord logRecord2 = new LogRecord(
            "file2", "192.168.1.1", "user2", ZonedDateTime.now(),
            "POST /resource2 HTTP/1.1", 404, 1500, "http://referer2", "Chrome"
        );
        accumulator1.accumulate(logRecord1);
        accumulator2.accumulate(logRecord2);

        accumulator1.combine(accumulator2);

        assertEquals(2, accumulator1.requestsCount());
        assertEquals(1000, accumulator1.getAverageResponsesSize(), 0.001);
        assertTrue(accumulator1.getRequestResourcesStatistics().containsKey("/resource1"));
        assertTrue(accumulator1.getRequestResourcesStatistics().containsKey("/resource2"));
        assertTrue(accumulator1.getResponseCodesStatistics().containsKey(200));
        assertTrue(accumulator1.getResponseCodesStatistics().containsKey(404));
        assertTrue(accumulator1.getRequestMethodsStatistics().containsKey("GET"));
        assertTrue(accumulator1.getRequestMethodsStatistics().containsKey("POST"));
        assertEquals(2, accumulator1.getUniqueIpsCount());
        assertTrue(accumulator1.filenames().contains("file1"));
        assertTrue(accumulator1.filenames().contains("file2"));
    }
}
