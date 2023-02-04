package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import com.google.gson.Gson;

public class Foods {

    private Food[] foods;

    public Food[] getFoods() {
        return foods;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
