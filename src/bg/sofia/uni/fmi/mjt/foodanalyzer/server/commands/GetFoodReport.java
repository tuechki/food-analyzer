package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.CacheStorage;

public class GetFoodReport extends AbstractCommand {
    public GetFoodReport(String command) {
        super(command);
    }

    @Override
    public String execute(CacheStorage cacheStorage, FDCClient fdcClient) {
        return "get-food-report";
    }
}
