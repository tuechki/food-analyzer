package bg.sofia.uni.fmi.mjt.foodanalyzer;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.FoodAnalyzerServer;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheInMemoryStorage;

import java.net.http.HttpClient;

public class Application {

    private static final int PORT = 7777;

    public static void main(String[] args) {
        CacheInMemoryStorage cacheInMemoryStorage = new CacheInMemoryStorage();
        FDCClient fdcClient = new FDCClient(HttpClient.newBuilder().build());

        FoodAnalyzerServer server = new FoodAnalyzerServer(PORT, cacheInMemoryStorage, fdcClient);

        server.start();


    }
}
