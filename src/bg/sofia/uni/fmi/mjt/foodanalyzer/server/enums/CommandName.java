package bg.sofia.uni.fmi.mjt.foodanalyzer.server.enums;

public enum CommandName {
    GET_FOOD("get-food"),
    GET_FOOD_REPORT("get-food-report"),
    GET_FOOD_BY_BARCODE("get-food-by-barcode");

    private final String name;
    CommandName(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }
}
