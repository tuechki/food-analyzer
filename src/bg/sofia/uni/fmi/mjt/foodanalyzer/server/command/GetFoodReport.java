package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.CacheStorage;

import java.net.URISyntaxException;

public class GetFoodReport extends AbstractCommand {

    public GetFoodReport(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(CacheStorage cacheStorage, FDCClient fdcClient) {
        String fdcId = getArguments()[0];
        FoodReport foodReport = cacheStorage.getFoodReportIfExists(fdcId);

        if (foodReport != null) {
            return GSON.toJson(foodReport);
        }

        try {
            foodReport = fdcClient.getFoodReport(fdcId);
            cacheStorage.cacheFoodReport(fdcId, foodReport);

            return GSON.toJson(foodReport);
        } catch (URISyntaxException | ApiKeyMissingException |
                 FoodRetrievalClientException | NoResultsFoundException | BadRequestException e) {
            return e.getMessage();
        }
    }

    @Override
    protected int getMinArg() {
        return 1;
    }

    @Override
    protected int getMaxArg() {
        return 1;
    }
}
