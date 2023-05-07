package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;

import java.util.List;

public interface CacheStorage {

    public void cacheFoods(String query, List<Food> foods);

    public void cacheFoodReport(String fdcId, FoodReport foodReport);

    public List<Food> getFoods(String query);

    public FoodReport getFoodReport(String fdcId);

    public FoodReport getFoodReportByBarcode(String gtinUpc);
}
