package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JSONParser {
    public static List<Food> toListOfFoods(String inputJson) {
        Type type = new TypeToken<List<Food>>() { }.getType();

        Gson gson = new GsonBuilder().registerTypeAdapter(type, new FoodDeserializer()).create();

        return gson.fromJson(inputJson, type);
    }

    public static FoodReport toFoodReport(String inputJson) {
        Gson gson = new Gson();

        FoodReport foodReport = gson.fromJson(inputJson, FoodReport.class);

        return foodReport;
    }
}
