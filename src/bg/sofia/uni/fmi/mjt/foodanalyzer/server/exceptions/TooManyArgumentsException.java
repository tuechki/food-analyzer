package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class TooManyArgumentsException extends Exception {
    public TooManyArgumentsException(int expected, int actual) {
        super("Too many arguments. Expected maximum " + expected + ", but was " + actual + ".");
    }
}
