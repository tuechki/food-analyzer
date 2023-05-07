package bg.sofia.uni.fmi.mjt.foodanalyzer.server.service.cache;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Food;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodNutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.FoodReport;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto.Nutrient;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.NutrientName;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums.Unit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CacheStorageTest {

    private static final String FDC_ID = "2041155";
    private static final String BARCODE = "009800146130";
    private static final String QUERY = "raffaello treat";
    private FoodDataCache storage;

    @BeforeEach
    public void setUp() {
        storage = new FoodDataStorage();
    }

    @Test
    public void testCacheFoodReportIsCached() {

        storage.cacheFoodReport(FDC_ID, getFoodReport());

        assertNotNull(storage.getFoodReport(FDC_ID),
            "FoodReports container should not be after cache operation.");
    }

    @Test
    public void testCacheFoodsAreCached() {

        storage.cacheFoods(QUERY, getFoods());

        assertNotNull(storage.getFoods(QUERY),
            "Foods container should not be after cache operation.");
    }


    @Test
    public void testGetFoodReportByBarcodeWhenCachedExists() {

        storage.cacheFoods(QUERY, getFoods());
        storage.cacheFoodReport(FDC_ID, getFoodReport());

        assertNotNull(storage.getFoodReportByBarcode(BARCODE),
            "FoodReport with the respective Barcode should be available after cache operations.");
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
