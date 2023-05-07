package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.CacheStorage;

import java.net.URISyntaxException;

public class GetFood extends AbstractCommand {

    private static final int MAX_ARG = 10;

    public GetFood(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(CacheStorage cacheStorage, FDCClient fdcClient) {
        String query = getQuery();
        Foods foods = cacheStorage.getFoodIfExists(query);

        if (foods != null) {
            return GSON.toJson(foods);
        }

        try {
            foods = fdcClient.getFood(query);
            cacheStorage.cacheFoods(query, foods);

            return GSON.toJson(foods);
            //TODO: Collapse these exceptions URISyntaxException | ApiKeyMissingException | from the API into one
        } catch (URISyntaxException | ApiKeyMissingException | FoodRetrievalClientException | BadRequestException e) {
            return e.getMessage();
        }
    }

    @Override
    protected int getMinArg() {
        return 1;
    }

    //TODO: Make it or them constants
    @Override
    protected int getMaxArg() {
        return MAX_ARG;
    }

    private String getQuery() {
        return String.join(" ", getArguments());
    }
}
