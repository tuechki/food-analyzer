package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.CommandName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnrecognizedCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheInMemoryStorage;

public interface Command {

    String execute(CacheInMemoryStorage cacheInMemoryStorage, FDCClient fdcClient) throws WrongArgumentsException;

    static Command of(String command)
        throws UnrecognizedCommandException, TooFewArgumentsException, TooManyArgumentsException {
        String userCommand = command.split("\s+")[0];
        CommandName commandName = CommandName.getByName(userCommand);
        return switch (commandName) {
            case GET_FOOD -> new GetFood(command);
            case GET_FOOD_REPORT -> new GetFoodReport(command);
            case GET_FOOD_BY_BARCODE -> new GetFoodByBarcode(command);
            case null -> throw new UnrecognizedCommandException(userCommand);
        };
    }
}
