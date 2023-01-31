package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

public class GetFoodReport extends AbstractCommand {
    public GetFoodReport(String command) {
        super(command);
    }

    @Override
    public String execute() {
        return "get-food-report";
    }
}
