package bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums;

import java.util.HashMap;
import java.util.Map;

public enum ArgumentFlag {
    CODE("--code"),
    IMG("--img");

    private static final Map<String, ArgumentFlag> LOOKUP = new HashMap<>();

    static {
        for (ArgumentFlag argumentFlag : values()) {
            LOOKUP.put(argumentFlag.getValue(), argumentFlag);
        }
    }

    private final String value;
    ArgumentFlag(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ArgumentFlag getByValue(String value) {
        return LOOKUP.get(value);
    }
}
