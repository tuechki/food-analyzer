package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import java.util.Arrays;

public abstract class AbstractCommand implements Command {
    private String command;
    private String[] arguments;

    public AbstractCommand(String command) {
        String[] splitCommand = command.split("\s+");
        this.command = splitCommand[0];
        this.arguments = (String[]) Arrays.stream(splitCommand).sequential().skip(1).toArray();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

}
