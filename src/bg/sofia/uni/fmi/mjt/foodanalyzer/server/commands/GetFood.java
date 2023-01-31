package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

public class GetFood extends AbstractCommand {
    public GetFood(String command) {
        super(command);
    }

    @Override
    public String execute() {
        return "get-food";
    }
}
