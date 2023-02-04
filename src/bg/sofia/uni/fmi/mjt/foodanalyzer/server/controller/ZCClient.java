package bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BarcodeRetrievalException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

public class ZCClient {

    public static void main(String[] args) {
        ZCClient zcClient1 = new ZCClient(HttpClient.newBuilder().build());

        try {
            zcClient1.getResponse("./images/upc-barcode.gif");
        } catch (BarcodeRetrievalException e) {
            throw new RuntimeException(e);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        } catch (NoResultsFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "zxing.org";
    private static final String API_ENDPOINT_PATH = "/w/decode";
    private static final Gson GSON = new Gson();

    private final HttpClient zcClient;

    public ZCClient(HttpClient zcClient) {
        this.zcClient = zcClient;
    }


    /**
     * Send a request to the ZXing service to parse image to barcode.
     *
     * @return HttpResponse object
     *
     * @throws BarcodeRetrievalException, BadRequestException
     */
    private HttpResponse<String> getResponse(String pathToImage)
        throws BarcodeRetrievalException, BadRequestException, NoResultsFoundException {
        HttpResponse<String> response;
        try {

            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH, null);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "multipart/form-data")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(pathToImage)))
                .build();

            response = zcClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new BarcodeRetrievalException("Could not retrieve barcode from this image.", e);
        }

        if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new BadRequestException("Bad query parameters.");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new NoResultsFoundException("Could not retrieve any results for this food or id.");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return response;
        }

        throw new BarcodeRetrievalException("Unexpected response code from ZXing service");
    }
}
