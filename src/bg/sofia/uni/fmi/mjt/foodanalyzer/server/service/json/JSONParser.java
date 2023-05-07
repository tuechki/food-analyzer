package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.json;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JSONParser {
    public static List<Food> toListOfFoods(String foodsJson) {
        Type type = new TypeToken<List<Food>>() { }.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(type, new FoodDeserializer()).create();

        return gson.fromJson(foodsJson, type);
    }

    public static FoodReport toFoodReport(String foodReportJson) {
        Gson gson = new Gson();

        return gson.fromJson(foodReportJson, FoodReport.class);
    }

    private static class FoodDeserializer implements JsonDeserializer<List<Food>> {

        @Override
        public List<Food> deserialize(JsonElement jsonElement, Type type,
                                      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonElement content = jsonElement.getAsJsonObject().get("foods");
            type = new TypeToken<List<Food>>() { }.getType();

            return new Gson().fromJson(content, type);
        }
    }
}
