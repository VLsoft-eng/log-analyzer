package backend.academy.logReaders.implementations;

import backend.academy.logReaders.abstractions.LogReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class LocalLogReader implements LogReader {

    private LogReader nextLogReader;

    @Override
    public Optional<Stream<String>> getLogLines(String path) {
        try {
            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + path);
            Path startPath = Paths.get(System.getProperty("user.dir"));

            try (Stream<Path> matchedPaths = Files.walk(startPath)) {
                Stream<String> lines = matchedPaths
                    .filter(Files::isRegularFile)
                    .filter(pathMatcher::matches)
                    .flatMap(this::processFile);

                return  Optional.of(lines);
            }
        } catch (IOException e) {
            return handleNextLogReader(path);
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

    private Stream<String> processFile(Path file) {
        try {
            return Files.lines(file);
        } catch (IOException e) {
            return Stream.empty();
        }
    }
}
