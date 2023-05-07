package bg.sofia.uni.fmi.mjt.foodanalyzer;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.command.Command;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnsupportedCommandException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommandTest {

    @Test
    public void testCommandCreationWithUnsupportedCommandThrowsException() {
        String command = "test";
        assertThrows(UnsupportedCommandException.class, () -> Command.of(command),
            "UnsupportedCommandException was not thrown when provided unrecognized command.");
    }

    private static Set<Arguments> provideCommand() {
        return Set.of(
            Arguments.of("get-food"),
            Arguments.of("get-food-report"),
            Arguments.of("get-food-by-barcode")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCommand")
    public void testCommandCreationWithTooFewArgumentsThrowsException(String command) {
        assertThrows(TooFewArgumentsException.class, () -> Command.of(command),
            "TooFewArgumentsException was not thrown when provided fewer than expected arguments.");
    }


    private static Set<Arguments> provideCommandWithArguments() {
        return Set.of(
            Arguments.of("get-food-report arg1 arg2"),
            Arguments.of("get-food-by-barcode arg1 arg2 arg3"),
            Arguments.of("get-food-report arg1 arg2 arg3")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCommandWithArguments")
    public void testCommandCreationWithTooManyArgumentsThrowsException(String command) {
        assertThrows(TooManyArgumentsException.class, () -> Command.of(command),
            "TooManyArgumentsException was not thrown when provided more than expected arguments.");
    }

}
