package bg.sofia.uni.fmi.mjt.foodanalyzer.server.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Food implements Serializable {

    @Serial
    private static final long serialVersionUID = 7610618888105857515L;

    private String description;
    private String fdcId;
    private String gtinUpc;

    public Food(String description, String fdcId, String gtinUpc) {
        this.description = description;
        this.fdcId = fdcId;
        this.gtinUpc = gtinUpc;
    }

    public String getFdcId() {
        return fdcId;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food food)) return false;
        return getFdcId().equals(food.getFdcId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFdcId());
    }
}
