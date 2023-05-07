package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class FoodReport implements Serializable {

    @Serial
    private static final long serialVersionUID = 2923133435977158048L;

    @SerializedName("description")
    private String name;

    private String ingredients;

    private FoodNutrient[] foodNutrients;

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodReport that)) return false;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static FoodReportBuilder builder(String name) {
        return new FoodReportBuilder(name);
    }

    private FoodReport(FoodReportBuilder builder) {
        this.name = builder.name;
        this.ingredients = builder.ingredients;
        this.foodNutrients = builder.foodNutrients;
    }

    public static class FoodReportBuilder {

        private final String name;
        private String ingredients;
        private FoodNutrient[] foodNutrients;

        private FoodReportBuilder(String name) {
            this.name = name;
        }

        public FoodReportBuilder setIngredients(String ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public FoodReportBuilder setFoodNutrients(FoodNutrient[]  foodNutrients) {
            this.foodNutrients = foodNutrients;
            return this;
        }

        public FoodReport build() {
            return new FoodReport(this);
        }

    }
}
