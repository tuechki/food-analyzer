package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.io.Serial;
import java.io.Serializable;

public record FoodNutrient(Nutrient nutrient, String amount) implements Serializable {

    @Serial
    private static final long serialVersionUID = -6100149948476986141L;

}
