package bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FDCClient {
    public static final String API_KEY = "LNnSmojdImXMsI09R5bReW4HQHIBBw3wLjwx77IG";
    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "api.nal.usda.gov";
    private static final String API_SEARCH_ENDPOINT_PATH = "/fdc/v1/foods/search";
    private static final String API_REPORT_ENDPOINT_PATH = "/fdc/v1/food/";
    private static final Gson GSON = new Gson();

    private final HttpClient foodHttpClient;
    private final String apiKey;

    public FDCClient(HttpClient foodHttpClient) {
        this(foodHttpClient, API_KEY);
    }

    public FDCClient(HttpClient foodHttpClient, String apiKey) {
        this.foodHttpClient = foodHttpClient;
        this.apiKey = apiKey;
    }


    /**
     * Get food - fetches basic info about all the foods matching a particular foodName
     * - fdcId, description, gtinUpc (if available)
     *
     * @return Foods object
     *
     * @throws FoodRetrievalClientException, ApiKeyMissingException, BadRequestException
     */
    public Foods getFood(String foodName)
        throws URISyntaxException,
                ApiKeyMissingException,
                FoodRetrievalClientException,
                BadRequestException {

        //Then make an API request
        URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_SEARCH_ENDPOINT_PATH,
            "query=" + foodName + "&requireAllWords=true" + "&api_key=" + this.apiKey, null);

        HttpResponse<String> response = getResponse(uri);

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(), Foods.class);
        }

        return null;
    }


    /**
     * Food report - fetches extensive information about a particular food by its fdcId
     * - ingredients, lipid, protein, carbohydrates, fiber and energy
     *
     * @return FoodReport object
     *
     * @throws FoodRetrievalClientException, ApiKeyMissingException, NoResultsFoundException, BadRequestException
     */
    public FoodReport getFoodReport(String fdcId)
        throws URISyntaxException,
               ApiKeyMissingException,
               FoodRetrievalClientException,
               NoResultsFoundException,
               BadRequestException {
        URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_REPORT_ENDPOINT_PATH + fdcId,
            "nutrients=203&" +
                  "nutrients=204&" +
                  "nutrients=205&" +
                  "nutrients=291&" +
                  "nutrients=208&" +
                  "api_key=" + this.apiKey, null);

        HttpResponse<String> response = getResponse(uri);

        if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new NoResultsFoundException("Could not retrieve any results for this food or id.");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return GSON.fromJson(response.body(), FoodReport.class);
        }

        return null;
    }

    /**
     * Fetches food information based on the URI Query passed.
     * You can create custom URI Query and send it using this method.
     *
     * @return HttpResponse object
     *
     * @throws FoodRetrievalClientException, ApiKeyMissingException, NoResultsFoundException, BadRequestException
     */
    private HttpResponse<String> getResponse(URI uri)
        throws FoodRetrievalClientException, ApiKeyMissingException, BadRequestException {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = foodHttpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new FoodRetrievalClientException("Could not retrieve information about particular food or foods.", e);
        }

        if (response.statusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new ApiKeyMissingException("API key is missing or is incorrect.");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new BadRequestException("Bad query parameters.");
        }

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return response;
        }

        throw new FoodRetrievalClientException("Unexpected response code from food central service");
    }

}
