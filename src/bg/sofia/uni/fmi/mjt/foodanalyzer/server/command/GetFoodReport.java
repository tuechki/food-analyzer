package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;

import java.net.URISyntaxException;

public class GetFoodReport extends AbstractCommand {

    public GetFoodReport(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(FoodDataCache cacheStorage, FDCClient fdcClient) {
        String fdcId = getArguments()[0];
        FoodReport foodReport = cacheStorage.getFoodReport(fdcId);

        if (foodReport != null) {
            return GSON.toJson(foodReport);
        }

        try {
            foodReport = fdcClient.getFoodReport(fdcId);
            cacheStorage.cacheFoodReport(fdcId, foodReport);

            return GSON.toJson(foodReport);
        } catch (URISyntaxException | ApiKeyMissingException e) {
            LOGGER.log(e.getMessage());
            return ("There was a problem with the server when processing your request. Please try again later.");
        } catch (BadRequestException e) {
            return ("Please check the validity of your arguments. Check for any special characters.");
        } catch (NoResultsFoundException e) {
            return String.format("Sorry, no food available with this id %s on the server.",  fdcId);
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
        return 1;
    }
}
