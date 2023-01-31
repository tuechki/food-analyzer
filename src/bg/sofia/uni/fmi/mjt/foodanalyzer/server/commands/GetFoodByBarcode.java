package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

public class GetFoodByBarcode extends AbstractCommand {

    public GetFoodByBarcode(String command) {
        super(command);
    }

    @Override
    public String execute() {
        return "get-food-by-barcode";
    }
}
