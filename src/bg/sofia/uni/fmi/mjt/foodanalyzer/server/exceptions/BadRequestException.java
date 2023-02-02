package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
