package org.pancakelab.model.pancakes;

public enum Ingredient {

    // already in the menu
    MILK_CHOCOLATE("Milk chocolate"),
    HAZELNUTS("Hazelnuts"),
    DARK_CHOCOLATE("Dark chocolate"),
    WHIPPED_CREAM("Whipped Cream"),

    // custom
    SALT("Salt"),
    SUGAR("Sugar"),
    BUTTER("Butter"),
    VANILLA("Vanilla"),
    MAPLE_SYRUP("Maple syrup");

    private final String description;

    Ingredient(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
