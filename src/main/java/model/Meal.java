package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meal {
    private String name;
    private double price;
    private double qnt;
    private String tipo;

    @Override
    public String toString() {
        return String.format("- %s - R$ %.2f (%.0f)", name, price, qnt);
    }
}

