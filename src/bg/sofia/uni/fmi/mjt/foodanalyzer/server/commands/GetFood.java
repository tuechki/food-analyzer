package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheStorage;

import java.net.URISyntaxException;

public class GetFood extends AbstractCommand {
    public GetFood(String command) {
        super(command);
    }

    @Override
    public String execute(CacheStorage cacheStorage, FDCClient fdcClient) {
        Food[] cacheFoods = cacheStorage.getFoodIfExists(getArgument());

        if (cacheFoods != null) {
            return GSON.toJson(cacheFoods);
        }

        try {
            Foods foods = fdcClient.getFood(getArgument());
            return GSON.toJson(foods);
        } catch (URISyntaxException | ApiKeyMissingException |
                 FoodRetrievalClientException | NoResultsFoundException | BadRequestException e) {
            return e.getMessage();
        }
    }
}
