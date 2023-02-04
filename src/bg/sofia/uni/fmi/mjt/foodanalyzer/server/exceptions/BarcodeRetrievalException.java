package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class BarcodeRetrievalException extends Exception {
    public BarcodeRetrievalException(String message) {
        super(message);
    }

    public BarcodeRetrievalException(String message, Exception e) {
        super(message, e);
    }
}
