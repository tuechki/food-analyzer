package bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.ArgumentFlag;

public class WrongBarcodeArgumentsException extends Exception {
    public WrongBarcodeArgumentsException() {
        super("Wrong arguments. Expected argument " + ArgumentFlag.CODE + " or" + ArgumentFlag.IMG + ".");
    }
}
