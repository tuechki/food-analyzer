package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.ArgumentFlag;

public class WrongArgumentsException extends Exception {
    public WrongArgumentsException() {
        super("Wrong arguments. Expected argument " + ArgumentFlag.CODE + " or" + ArgumentFlag.IMG + ".");
    }
}
