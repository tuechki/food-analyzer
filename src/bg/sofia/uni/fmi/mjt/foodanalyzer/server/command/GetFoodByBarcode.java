package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.ArgumentFlag;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.CacheStorage;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.ZCService;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetFoodByBarcode extends AbstractCommand {

    public GetFoodByBarcode(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(CacheStorage cacheStorage, FDCClient fdcClient) throws WrongArgumentsException {
        String gtinUpc = getGtinUpc();
        FoodReport foodReport = cacheStorage.getFoodReportByBarcodeIfExists(gtinUpc);

        if (foodReport != null) {
            return GSON.toJson(foodReport);
        }

        return "Sorry, no food with this barcode " + gtinUpc + " on the server.";
    }

    @Override
    protected int getMinArg() {
        return 1;
    }

    @Override
    protected int getMaxArg() {
        return 2;
    }

    //TODO: Parse arguments correctly to extract barcode.
    private String getGtinUpc() throws WrongArgumentsException {

        Map<ArgumentFlag, String> argumentFlagValueMap = new HashMap<>();

        for (String argument : getArguments()) {
            String[] splitArgument = argument.split("=");
            ArgumentFlag argumentFlag = ArgumentFlag.getByValue(splitArgument[0]);
            argument = splitArgument[1];

            argumentFlagValueMap.put(argumentFlag, argument);
        }

        if (argumentFlagValueMap.containsKey(ArgumentFlag.IMG)) {

            ZCService zcService = new ZCService();
            try {
                return zcService.getBarcode(argumentFlagValueMap.get(ArgumentFlag.IMG));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            } catch (FormatException e) {
                throw new RuntimeException(e);
            } catch (ChecksumException e) {
                throw new RuntimeException(e);
            }
        }

        if (argumentFlagValueMap.containsKey(ArgumentFlag.CODE)) {
            return argumentFlagValueMap.get(ArgumentFlag.CODE);
        }

        throw new WrongArgumentsException();
    }
}
