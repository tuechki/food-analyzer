package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;

import java.util.List;

public interface FoodDataCache {

    void cacheFoods(String query, List<Food> foods);

    void cacheFoodReport(String fdcId, FoodReport foodReport);

    List<Food> getFoods(String query);

    FoodReport getFoodReport(String fdcId);

    FoodReport getFoodReportByBarcode(String gtinUpc);
}
