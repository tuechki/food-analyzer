package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import com.google.gson.Gson;

public class FoodReport {
    private String description;
    private String ingredients;
    private FoodNutrient[] foodNutrients;

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
