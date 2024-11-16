package backend.academy.utilities.logReaders.implementations;

import backend.academy.records.LineRecord;
import backend.academy.utilities.logReaders.abstractions.LogReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LocalLogReader implements LogReader {

    private LogReader nextLogReader;

    @Override
    public Optional<Stream<LineRecord>> getLogLines(String path) {
        try {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + path);
            Path startPath = Paths.get(System.getProperty("user.dir"));

            List<Path> matchedPaths = Files.walk(startPath)
                .filter(Files::isRegularFile)
                .filter(pathMatcher::matches)
                .toList();

            if (matchedPaths.isEmpty()) {
                return handleNextLogReader(path);
            }

            Stream<LineRecord> lines = matchedPaths
                .stream()
                .flatMap(this::processFile);

            return Optional.of(lines);
        } catch (IOException e) {
            return handleNextLogReader(path);
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

    private Stream<LineRecord> processFile(Path file) {
        try {
            String filename = file.getFileName().toString();
            return Files.lines(file, StandardCharsets.UTF_8)
                .map(line -> new LineRecord(filename, line));
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}
