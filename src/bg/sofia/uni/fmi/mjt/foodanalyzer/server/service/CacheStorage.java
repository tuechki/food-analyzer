package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Foods;

public interface CacheStorage {

    public void cacheFoods(String query, Foods foods);

    public void cacheFoodReport(String fdcId, FoodReport foodReport);

    public Foods getFoodIfExists(String query);

    public FoodReport getFoodReportIfExists(String fdcId);

    public FoodReport getFoodReportByBarcodeIfExists(String gtinUpc);
}
