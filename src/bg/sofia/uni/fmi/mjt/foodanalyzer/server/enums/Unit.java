package bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums;
public enum Unit {
    GRAM("g"),
    KCAL("kcal");

    private final String name;
    Unit(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
