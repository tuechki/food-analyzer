package bg.sofia.uni.fmi.mjt.foodanalyzer;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataStorage;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;

import java.net.http.HttpClient;

public class Application {

    private static final int PORT = 6666;

    public static void main(String[] args) {
        FoodDataCache cacheStorage = new FoodDataStorage();
        FDCClient fdcClient = new FDCClient(HttpClient.newBuilder().build());

        FoodAnalyzerServer server = new FoodAnalyzerServer(PORT, cacheStorage, fdcClient);

        server.start();

    }
}
