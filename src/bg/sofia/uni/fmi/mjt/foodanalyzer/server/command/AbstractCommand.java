package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.Logger;
import com.google.gson.Gson;

import java.util.Arrays;

public abstract class AbstractCommand implements Command {

    protected static final Logger LOGGER = Logger.getInstance();
    protected static final Gson GSON = new Gson();
    private String command;
    private String[] arguments;

    public AbstractCommand(String command) throws TooFewArgumentsException, TooManyArgumentsException {
        String[] splitCommand = command.split("\s+");
        this.command = splitCommand[0];
        this.arguments = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);

        if (hasTooFewArguments()) {
            throw new TooFewArgumentsException(getMinArg(), getArguments().length);
        }

        if (hasTooManyArguments()) {
            throw new TooManyArgumentsException(getMaxArg(), getArguments().length);
        }
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

    protected abstract int getMinArg();
    protected abstract int getMaxArg();
    private boolean hasTooFewArguments() {
        return getArguments().length < getMinArg();
    }
    private boolean hasTooManyArguments() {
        return getArguments().length > getMaxArg();
    }
}
