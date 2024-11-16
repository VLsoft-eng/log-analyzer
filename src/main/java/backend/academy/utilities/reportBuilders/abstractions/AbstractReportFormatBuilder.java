package backend.academy.utilities.reportBuilders.abstractions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractReportFormatBuilder {
    protected static final Map<Integer, String> HTTP_STATUS_DESCRIPTIONS = Map.<Integer, String>ofEntries(
        Map.entry(100, "Continue"),
        Map.entry(101, "Switching Protocols"),
        Map.entry(102, "Processing"),
        Map.entry(103, "Early Hints"),
        Map.entry(200, "OK"),
        Map.entry(201, "Created"),
        Map.entry(202, "Accepted"),
        Map.entry(203, "Non-Authoritative Information"),
        Map.entry(204, "No Content"),
        Map.entry(205, "Reset Content"),
        Map.entry(206, "Partial Content"),
        Map.entry(207, "Multi-Status"),
        Map.entry(208, "Already Reported"),
        Map.entry(226, "IM Used"),
        Map.entry(300, "Multiple Choices"),
        Map.entry(301, "Moved Permanently"),
        Map.entry(302, "Found"),
        Map.entry(303, "See Other"),
        Map.entry(304, "Not Modified"),
        Map.entry(305, "Use Proxy"),
        Map.entry(307, "Temporary Redirect"),
        Map.entry(308, "Permanent Redirect"),
        Map.entry(400, "Bad Request"),
        Map.entry(401, "Unauthorized"),
        Map.entry(402, "Payment Required"),
        Map.entry(403, "Forbidden"),
        Map.entry(404, "Not Found"),
        Map.entry(405, "Method Not Allowed"),
        Map.entry(406, "Not Acceptable"),
        Map.entry(407, "Proxy Authentication Required"),
        Map.entry(408, "Request Timeout"),
        Map.entry(409, "Conflict"),
        Map.entry(410, "Gone"),
        Map.entry(411, "Length Required"),
        Map.entry(412, "Precondition Failed"),
        Map.entry(413, "Payload Too Large"),
        Map.entry(414, "URI Too Long"),
        Map.entry(415, "Unsupported Media Type"),
        Map.entry(416, "Range Not Satisfiable"),
        Map.entry(417, "Expectation Failed"),
        Map.entry(418, "I'm a Teapot"),
        Map.entry(421, "Misdirected Request"),
        Map.entry(422, "Unprocessable Entity"),
        Map.entry(423, "Locked"),
        Map.entry(424, "Failed Dependency"),
        Map.entry(425, "Too Early"),
        Map.entry(426, "Upgrade Required"),
        Map.entry(428, "Precondition Required"),
        Map.entry(429, "Too Many Requests"),
        Map.entry(431, "Request Header Fields Too Large"),
        Map.entry(451, "Unavailable For Legal Reasons"),
        Map.entry(500, "Internal Server Error"),
        Map.entry(501, "Not Implemented"),
        Map.entry(502, "Bad Gateway"),
        Map.entry(503, "Service Unavailable"),
        Map.entry(504, "Gateway Timeout"),
        Map.entry(505, "HTTP Version Not Supported"),
        Map.entry(506, "Variant Also Negotiates"),
        Map.entry(507, "Insufficient Storage"),
        Map.entry(508, "Loop Detected"),
        Map.entry(510, "Not Extended"),
        Map.entry(511, "Network Authentication Required")
    );
    protected static final String MOST_REQUESTED_RESOURCES = "Самые частые запрашиваемые ресурсы";
    protected static final String MOST_RESPONSES_CODES = "Самые частые коды ответа";
    protected static final String MOST_REQUESTS_METHODS = "Самые частые методы запроса";

    protected final StringBuilder stringBuilder = new StringBuilder();
    protected final Map<String, String> metrics = new LinkedHashMap<>();
    protected final List<String> requestedResources = new ArrayList<>();
    protected final List<String> responseCodes = new ArrayList<>();
    protected final List<String> requestMethods = new ArrayList<>();

    public void addFilenames(Set<String> filenames) {
        String formattedFilenames = filenames.stream()
            .map(filename -> "`" + filename + "`")
            .collect(Collectors.joining(", "));
        metrics.put("Файл(-ы)", formattedFilenames);
    }

    public void addStartDate(String startDate) {
        metrics.put("Начальная дата", startDate != null ? startDate : "-");
    }

    public void addEndDate(String endDate) {
        metrics.put("Конечная дата", endDate != null ? endDate : "-");
    }

    public void addRequestsCount(long requestsCount) {
        metrics.put("Количество запросов", String.valueOf(requestsCount));
    }

    public void addUniqueIpsCount(long uniqueIpsCount) {
        metrics.put("Количество уникальных IP", String.valueOf(uniqueIpsCount));
    }

    public void addAverageResponsesSize(Double averageResponsesSize) {
        metrics.put("Средний размер ответа", averageResponsesSize + "b");
    }

    public void addResponsePercentile(Double responsePercentile) {
        metrics.put("95p размера ответа", responsePercentile + "b");
    }
}
