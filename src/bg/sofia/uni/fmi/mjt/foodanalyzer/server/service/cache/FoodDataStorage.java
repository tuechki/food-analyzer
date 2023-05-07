package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.json.JSONParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDataStorage implements FoodDataCache {

    private static final Path FILE_PATH_FOODS = Path.of("cacheFoods.txt");
    private static final Path FILE_PATH_FOOD_REPORTS = Path.of("cacheFoodReports.txt");

    private static final String SEPARATOR = "<>";

    private Map<String, List<Food>> foodsMap;
    private Map<String, FoodReport> foodReportMap;

    private Gson gson = new Gson();

    public FoodDataStorage() {
        this.foodsMap = new HashMap<>();
        this.foodReportMap = new HashMap<>();

        createIfNotExists(FILE_PATH_FOODS);
        createIfNotExists(FILE_PATH_FOOD_REPORTS);

        try (var foodsReader =
                 new BufferedReader(new FileReader(FILE_PATH_FOODS.toFile()));
             var foodReportsReader =
                 new BufferedReader(new FileReader(FILE_PATH_FOOD_REPORTS.toFile()))) {

            String line;
            while ((line = foodsReader.readLine()) != null) {
                loadFoodsInMemory(line);
            }

            while ((line = foodReportsReader.readLine()) != null) {
                loadFoodReportInMemory(line);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cacheFoods(String query, List<Food> foods) {

        foodsMap.putIfAbsent(query, foods);
        writeToFile(query + SEPARATOR + gson.toJson(foods), FILE_PATH_FOODS);
    }

    public void cacheFoodReport(String fdcId, FoodReport foodReport) {
        foodReportMap.putIfAbsent(fdcId, foodReport);
        writeToFile(fdcId + SEPARATOR + foodReport.toJson(), FILE_PATH_FOOD_REPORTS);
    }

    public List<Food> getFoods(String query) {
        return foodsMap.getOrDefault(query, null);
    }

    public FoodReport getFoodReport(String fdcId) {
        return foodReportMap.getOrDefault(fdcId, null);
    }

    public FoodReport getFoodReportByBarcode(String gtinUpc) {

        String fdcId = null;
        for (var entrySet : foodsMap.entrySet()) {
            for (var food : entrySet.getValue()) {
                if (gtinUpc.equals(food.getGtinUpc())) {
                    fdcId = food.getFdcId();
                }
            }
        }

        return fdcId != null ? getFoodReport(fdcId) : null;
    }

    protected static void writeToFile(String text, Path pathToFile) {
        try (var bufferedWriter = Files.newBufferedWriter(pathToFile)) {
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    private void loadFoodsInMemory(String line) {
        String[] parsed = line.split(SEPARATOR);
        Type type = new TypeToken<List<Food>>() { }.getType();
        foodsMap.putIfAbsent(parsed[0], gson.fromJson(parsed[1], type));
    }

    private void loadFoodReportInMemory(String line) {
        String[] parsed = line.split(SEPARATOR);
        foodReportMap.putIfAbsent(parsed[0], JSONParser.toFoodReport(parsed[1]));
    }

    private void createIfNotExists(Path pathToFile) {
        if (Files.notExists(FILE_PATH_FOODS)) {
            try {
                Files.createFile(FILE_PATH_FOODS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


