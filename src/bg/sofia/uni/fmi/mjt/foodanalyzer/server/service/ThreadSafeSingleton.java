package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ThreadSafeSingleton {
    private static final Path FILE_PATH = Path.of("cacheFoodReports.txt");
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() { }

    private synchronized static void writeToFile(String text) {
        try (var bufferedWriter = Files.newBufferedWriter(FILE_PATH)) {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    private synchronized static void readFromFile() {
        try (var bufferedReader = Files.newBufferedReader(FILE_PATH)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }
    }

    public static ThreadSafeSingleton getInstanceUsingDoubleLocking() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }

}


