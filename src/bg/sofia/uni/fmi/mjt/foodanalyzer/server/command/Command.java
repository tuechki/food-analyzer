package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.CommandName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnsupportedCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongBarcodeArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;

public interface Command {
    String execute(FoodDataCache cacheStorage, FDCClient fdcClient) throws WrongBarcodeArgumentsException;

    static Command of(String command)
        throws UnsupportedCommandException, TooFewArgumentsException, TooManyArgumentsException {
        String userCommand = command.split("\s+")[0];
        CommandName commandName = CommandName.getByName(userCommand);
        return switch (commandName) {
            case GET_FOOD -> new GetFood(command);
            case GET_FOOD_REPORT -> new GetFoodReport(command);
            case GET_FOOD_BY_BARCODE -> new GetFoodByBarcode(command);
            case null -> throw new UnsupportedCommandException(userCommand);
        };
    }
}
