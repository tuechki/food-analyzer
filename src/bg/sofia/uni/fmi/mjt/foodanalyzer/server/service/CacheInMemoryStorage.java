package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;

import java.util.concurrent.ConcurrentHashMap;

public class CacheInMemoryStorage implements CacheStorage {

    private ConcurrentHashMap<String, Foods> foodsMap;
    private ConcurrentHashMap<String, FoodReport> foodReportMap;

    public CacheInMemoryStorage() {
        this.foodsMap = new ConcurrentHashMap<>();
        this.foodReportMap = new ConcurrentHashMap<>();
    }

    public void cacheFoods(String query, Foods foods) {
        foodsMap.putIfAbsent(query, foods);

    }

    public void cacheFoodReport(String fdcId, FoodReport foodReport) {
        foodReportMap.putIfAbsent(fdcId, foodReport);
    }


    public Foods getFoodIfExists(String query) {
        return foodsMap.getOrDefault(query, null);
    }

    public FoodReport getFoodReportIfExists(String fdcId) {
        return foodReportMap.getOrDefault(fdcId, null);
    }

    public FoodReport getFoodReportByBarcodeIfExists(String gtinUpc) {

        String fdcId = null;
        for (var entrySet : foodsMap.entrySet()) {
            for (var food : entrySet.getValue().getFoods()) {
                if (gtinUpc.equals(food.getGtinUpc())) {
                    fdcId = food.getFdcId();
                }
            }
        }

        return fdcId != null ? getFoodReportIfExists(fdcId) : null;
    }
}
