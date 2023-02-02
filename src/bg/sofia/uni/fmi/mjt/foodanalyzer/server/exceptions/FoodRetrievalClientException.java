package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

public class FoodRetrievalClientException extends Exception {

    public FoodRetrievalClientException(String message) {
        super(message);
    }

    public FoodRetrievalClientException(String message, Exception e) {
        super(message, e);
    }
}
