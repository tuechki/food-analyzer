package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class TooFewArgumentsException extends Exception {
    public TooFewArgumentsException(int expected, int actual) {
        super("Too few arguments. Expected minimum " + expected + ", but was " + actual + ".");
    }
}
