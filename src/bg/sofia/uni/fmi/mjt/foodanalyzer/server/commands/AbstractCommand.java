package bg.sofia.uni.fmi.mjt.foodanalyzer.server.commands;

import com.google.gson.Gson;

import java.util.Arrays;

public abstract class AbstractCommand implements Command {

    protected static final Gson GSON = new Gson();
    private String command;
//    private String[] arguments;
    private String argument;

    public AbstractCommand(String command) {
        String[] splitCommand = command.split("\s+", 2);
        this.command = splitCommand[0];
        this.argument = splitCommand[1];
        //(String[]) Arrays.stream(splitCommand).sequential().skip(1).toArray();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

//    public String[] getArguments() {
//        return arguments;
//    }
//
//    public void setArguments(String[] arguments) {
//        this.arguments = arguments;
//    }

}
