package bg.sofia.uni.fmi.mjt.foodanalyzer.server.command;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.controller.FDCClient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodNutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.NutrientName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.Unit;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.ApiKeyMissingException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.BadRequestException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.FoodRetrievalClientException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.NoResultsFoundException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooFewArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.TooManyArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.UnsupportedCommandException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.exceptions.WrongBarcodeArgumentsException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache.FoodDataCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    private static final String FDC_ID = "2041155";

    private static final String QUERY = "raffaello treat";
    @Mock
    private FDCClient fdcClientMock;

    @Mock
    private FoodDataCache foodDataCache;

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


    @Test
    public void testGetFoodReportCommandCallsCacheService() throws
        ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException, TooFewArgumentsException,
        UnsupportedCommandException, TooManyArgumentsException, WrongBarcodeArgumentsException {

        when(foodDataCache.getFoodReport(anyString())).thenReturn(getFoodReport());

        Command command = Command.of("get-food-report 2041155");

        command.execute(foodDataCache, fdcClientMock);

        verify(foodDataCache, times(1)).getFoodReport(FDC_ID);
        verify(fdcClientMock, times(0)).getFoodReport(FDC_ID);
    }

    @Test
    public void testGetFoodCommandCallsCacheService() throws
        ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException, TooFewArgumentsException,
        UnsupportedCommandException, TooManyArgumentsException, WrongBarcodeArgumentsException {

        when(foodDataCache.getFoods(anyString())).thenReturn(getFoods());

        Command command = Command.of("get-food raffaello treat");

        command.execute(foodDataCache, fdcClientMock);

        verify(foodDataCache, times(1)).getFoods(QUERY);
        verify(fdcClientMock, times(0)).getFoods(QUERY);
    }



    @Test
    public void testGetFoodReportCommandCallsApiGetFoodReportMethod() throws
        ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException, TooFewArgumentsException,
        UnsupportedCommandException, TooManyArgumentsException, WrongBarcodeArgumentsException {

        when(foodDataCache.getFoodReport(anyString())).thenReturn(null);
        when(fdcClientMock.getFoodReport(anyString())).thenReturn(getFoodReport());

        Command command = Command.of("get-food-report 2041155");

        command.execute(foodDataCache, fdcClientMock);

        verify(foodDataCache, times(1)).getFoodReport(FDC_ID);
        verify(fdcClientMock, times(1)).getFoodReport(FDC_ID);
    }

    @Test
    public void testGetFoodCommandCallsApiGetFoodMethod() throws
        ApiKeyMissingException, FoodRetrievalClientException,
        NoResultsFoundException, BadRequestException, URISyntaxException, TooFewArgumentsException,
        UnsupportedCommandException, TooManyArgumentsException, WrongBarcodeArgumentsException {


        when(foodDataCache.getFoods(anyString())).thenReturn(null);
        when(fdcClientMock.getFoods(anyString())).thenReturn(getFoods());

        Command command = Command.of("get-food raffaello treat");

        command.execute(foodDataCache, fdcClientMock);

        verify(foodDataCache, times(1)).getFoods(QUERY);
        verify(fdcClientMock, times(1)).getFoods(QUERY);
    }


    private static FoodReport getFoodReport() {
        Nutrient proteinNutrient = new Nutrient(NutrientName.PROTEIN.getName(), Unit.GRAM.getName());
        Nutrient lipidNutrient = new Nutrient(NutrientName.LIPID.getName(), Unit.GRAM.getName());
        Nutrient carbohydrateNutrient = new Nutrient(NutrientName.CARBOHYDRATE.getName(), Unit.GRAM.getName());
        Nutrient fiberNutrient = new Nutrient(NutrientName.FIBER.getName(), Unit.GRAM.getName());
        Nutrient energyNutrient = new Nutrient(NutrientName.ENERGY.getName(), Unit.KCAL.getName());

        FoodNutrient protein = new FoodNutrient(proteinNutrient, "6.67000000");
        FoodNutrient lipid = new FoodNutrient(lipidNutrient, "50.00000000");
        FoodNutrient carbohydrate = new FoodNutrient(carbohydrateNutrient, "40.00000000");
        FoodNutrient fiber = new FoodNutrient(fiberNutrient, "3.30000000");
        FoodNutrient energy = new FoodNutrient(energyNutrient, "633.00000000");


        return FoodReport.builder("RAFFAELLO, ALMOND COCONUT TREAT")
            .setIngredients("VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.")
            .setFoodNutrients(new FoodNutrient[] {protein, lipid, carbohydrate, fiber, energy})
            .build();
    }

    private static List<Food> getFoods() {
        Food food = new Food("RAFFAELLO, ALMOND COCONUT TREAT", "2041155", "009800146130");

        return List.of(food);
    }

}
