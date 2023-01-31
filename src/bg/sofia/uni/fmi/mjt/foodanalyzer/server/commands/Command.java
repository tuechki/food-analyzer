package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.CommandName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnrecognizedCommandException;

public interface Command {

    String execute();

    static Command of(String command) throws UnrecognizedCommandException {
        CommandName commandName = CommandName.valueOf(command.split("\s+")[0]);
        return switch (commandName) {
            case GET_FOOD -> new GetFood(command);
            case GET_FOOD_REPORT -> new GetFoodReport(command);
            case GET_FOOD_BY_BARCODE -> new GetFoodByBarcode(command);
            default -> throw new UnrecognizedCommandException(commandName.getName());
        };
    }
}
