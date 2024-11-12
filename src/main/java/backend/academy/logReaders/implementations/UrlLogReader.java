package backend.academy.logReaders.implementations;

import backend.academy.logReaders.abstractions.LogReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class UrlLogReader implements LogReader {

    private LogReader nextLogReader;

    private static final int SUCCESS_CODE = 200;
    public static final String HTTP_SCHEME = "http";
    public static final String HTTPS_SCHEME = "https";

    @Override
    public Optional<Stream<String>> getLogLines(String url) {
        if (!isValidUrl(url)) {
            return handleNextLogReader(url);
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(URI.create(url));
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == SUCCESS_CODE) {
                    Stream<String> lines = getStringStream(response);
                    return Optional.of(lines);
                } else {
                    return handleNextLogReader(url);
                }
            }
        } catch (IOException e) {
            return handleNextLogReader(url);
        }
    }

    @Override
    public void setNextLogReader(LogReader reader) {
        this.nextLogReader = reader;

    }

    private Optional<Stream<String>> handleNextLogReader(String path) {
        if (nextLogReader != null) {
            return nextLogReader.getLogLines(path);
        } else {
            return Optional.empty();
        }
    }

    private Stream<String> getStringStream(CloseableHttpResponse response) {
        try (InputStream inputStream = response.getEntity().getContent();
             Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines();
        } catch (IOException e) {
            return Stream.empty();
        }
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
}
