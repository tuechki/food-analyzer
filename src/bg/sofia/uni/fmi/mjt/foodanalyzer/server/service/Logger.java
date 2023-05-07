package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Logger {

    private static final Path LOG = Path.of(".log");
    private static Logger instance = new Logger();
    private Logger() { }

    public static Logger getInstance() {
        return instance;
    }

    public void log(String log) {
        try (var bufferedWriter = Files.newBufferedWriter(LOG)) {
            bufferedWriter.write(log + System.lineSeparator());
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while trying to log messages.", e);
        }
    }
}
