package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;

import java.util.concurrent.ConcurrentHashMap;

public class CacheStorage {

    private ConcurrentHashMap<String, Food[]> foodsMap;
    private ConcurrentHashMap<String, FoodReport> foodReportMap;

    public CacheStorage() {
        this.foodsMap = new ConcurrentHashMap<>();
        this.foodReportMap = new ConcurrentHashMap<>();
    }


    public void cacheFoods(String query, Food[] foods) {
        foodsMap.putIfAbsent(query, foods);
    }

    public void cacheFoodReport(String fdcId, FoodReport foodReport) {
        foodReportMap.putIfAbsent(fdcId, foodReport);
    }


    public Food[] getFoodIfExists(String query) {
        return foodsMap.getOrDefault(query, null);
    }

    public FoodReport getFoodReportIfExists(String fdcId) {
        return foodReportMap.getOrDefault(fdcId, null);
    }
}
