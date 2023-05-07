package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.CommandName;

public class UnsupportedCommandException extends Exception {

    public UnsupportedCommandException(String command) {
        super("The command " + command + "is not supported." + System.lineSeparator() +
            "The supported commands up to this moment are: " + CommandName.GET_FOOD.getName() + ", "
                                                             + CommandName.GET_FOOD_REPORT.getName() + ", "
                                                             + CommandName.GET_FOOD_BY_BARCODE.getName());
    }
}
