package backend.academy.utilities.logReaders.implementations;

import backend.academy.utilities.logReaders.abstractions.LogReader;
import backend.academy.records.LineRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UrlLogReader implements LogReader {

    private LogReader nextLogReader;

    private static final int SUCCESS_CODE = 200;
    public static final String HTTP_SCHEME = "http";
    public static final String HTTPS_SCHEME = "https";
    private static final String URL_RESOURCE_REGEX = "/([^/]+)$";
    private static final String DEFAULT_RESOURCE = "default resource name";

    @Override
    public Optional<Stream<LineRecord>> getLogLines(String url) {
        if (!isValidUrl(url)) {
            return handleNextLogReader(url);
        }

        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();

            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == SUCCESS_CODE) {
                InputStream inputStream = response.body();
                Stream<String> stringStream = getStringStream(inputStream);
                String resourceName = getResourceNameFromUrl(url);

                Stream<LineRecord> lineRecordStream = stringStream.map(line -> new LineRecord(resourceName, line));
                return Optional.of(lineRecordStream.onClose(() -> closeStream(inputStream)));
            }
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private void closeStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setNextLogReader(LogReader reader) {
        this.nextLogReader = reader;
    }

    private Optional<Stream<LineRecord>> handleNextLogReader(String path) {
        if (nextLogReader != null) {
            return nextLogReader.getLogLines(path);
        } else {
            return Optional.empty();
        }
    }

    private Stream<String> getStringStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return bufferedReader.lines();
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            return uri.isAbsolute()
                && (HTTP_SCHEME.equalsIgnoreCase(scheme)
                || HTTPS_SCHEME.equalsIgnoreCase(scheme));
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private String getResourceNameFromUrl(String url) {
        Pattern pattern = Pattern.compile(URL_RESOURCE_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return DEFAULT_RESOURCE;
    }
}
