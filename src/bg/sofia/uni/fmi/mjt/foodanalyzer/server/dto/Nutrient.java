package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.io.Serial;
import java.io.Serializable;

public record Nutrient(String name, String unitName) implements Serializable {

    @Serial
    private static final long serialVersionUID = 5633290859060757943L;

}
