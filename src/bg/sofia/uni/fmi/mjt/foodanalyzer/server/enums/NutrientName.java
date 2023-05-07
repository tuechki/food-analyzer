package bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums;

public enum NutrientName {
    PROTEIN("Protein"),
    LIPID("Total lipid (fat)"),
    CARBOHYDRATE("Carbohydrate, by difference"),
    FIBER("Fiber, total dietary"),
    ENERGY("Energy");

    private final String name;
    NutrientName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
