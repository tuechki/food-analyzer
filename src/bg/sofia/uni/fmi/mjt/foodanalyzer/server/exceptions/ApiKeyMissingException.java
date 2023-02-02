package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class ApiKeyMissingException extends Exception {
    public ApiKeyMissingException(String message) {
        super(message);
    }
}
