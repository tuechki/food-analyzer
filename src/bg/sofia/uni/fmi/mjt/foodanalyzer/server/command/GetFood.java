package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;

import java.net.URISyntaxException;
import java.util.List;

public class GetFood extends AbstractCommand {

    private static final int MAX_ARG = 10;

    public GetFood(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(FoodDataCache cacheStorage, FDCClient fdcClient) {
        String query = getQuery();
        List<Food> foods = cacheStorage.getFoods(query);

        if (foods != null) {
            return GSON.toJson(foods);
        }

        try {
            foods = fdcClient.getFoods(query);
            cacheStorage.cacheFoods(query, foods);

            return GSON.toJson(foods);

        } catch (URISyntaxException | ApiKeyMissingException e) {
            LOGGER.log(e.getMessage());
            return ("There was a problem with the server when processing your request. Please try again later.");
        } catch (BadRequestException e) {
            return ("Please check the validity of your arguments. Check for any special characters.");
        } catch (NoResultsFoundException e) {
            return String.format("Sorry, no foods available for this query %s on the server.", getQuery());
        } catch (FoodRetrievalClientException e) {
            return e.getMessage();
        }
    }

    @Override
    protected int getMinArg() {
        return 1;
    }

    @Override
    protected int getMaxArg() {
        return MAX_ARG;
    }

    private String getQuery() {
        return String.join(" ", getArguments());
    }
}
