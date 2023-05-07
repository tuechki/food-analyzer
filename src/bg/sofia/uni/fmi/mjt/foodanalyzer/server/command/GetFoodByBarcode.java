package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.ArgumentFlag;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongBarcodeArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.ZXingService;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetFoodByBarcode extends AbstractCommand {

    private static final String EQUALS_SIGN = "=";

    public GetFoodByBarcode(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        super(command);
    }

    @Override
    public String execute(FoodDataCache cacheStorage, FDCClient fdcClient) throws WrongBarcodeArgumentsException {
        String gtinUpc = getGtinUpc();
        FoodReport foodReport = cacheStorage.getFoodReportByBarcode(gtinUpc);

        if (foodReport != null) {
            return GSON.toJson(foodReport);
        }

        return String.format("Sorry, no food available with this barcode %s on the server.",  gtinUpc);
    }

    @Override
    protected int getMinArg() {
        return 1;
    }

    @Override
    protected int getMaxArg() {
        return 2;
    }

    private String getGtinUpc() throws WrongBarcodeArgumentsException {

        Map<ArgumentFlag, String> argumentFlagValueMap = new HashMap<>();

        for (String argument : getArguments()) {
            String[] splitArgument = argument.split(EQUALS_SIGN);
            ArgumentFlag argumentFlag = ArgumentFlag.getByValue(splitArgument[0]);
            argument = splitArgument[1];

            argumentFlagValueMap.put(argumentFlag, argument);
        }

        if (argumentFlagValueMap.containsKey(ArgumentFlag.IMG)) {

            ZXingService zcService = ZXingService.getInstance();

            try {
                return zcService.getBarcode(argumentFlagValueMap.get(ArgumentFlag.IMG));
            } catch (IOException | FormatException | NotFoundException | ChecksumException e) {
                throw new RuntimeException(e);
            }
        }

        if (argumentFlagValueMap.containsKey(ArgumentFlag.CODE)) {
            return argumentFlagValueMap.get(ArgumentFlag.CODE);
        }

        throw new WrongBarcodeArgumentsException();
    }
}
