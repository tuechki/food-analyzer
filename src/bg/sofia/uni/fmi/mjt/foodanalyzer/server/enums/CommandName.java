package bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums;

import java.util.HashMap;
import java.util.Map;

public enum CommandName {
    GET_FOOD("get-food"),
    GET_FOOD_REPORT("get-food-report"),
    GET_FOOD_BY_BARCODE("get-food-by-barcode");

    private static final Map<String, CommandName> LOOKUP = new HashMap<>();

    static {
        for (CommandName commandName : values()) {
            LOOKUP.put(commandName.getName(), commandName);
        }
    }

    private final String name;
    CommandName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static CommandName getByName(String name) {
        return LOOKUP.get(name);
    }
}
