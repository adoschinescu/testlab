package org.pancakelab.model.pancakes;

import org.pancakelab.model.pancakes.exception.PancakeNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PancakeFactory {

    private static final String DARK_CHOCOLATE_PANCAKE = "DARK_CHOCOLATE_PANCAKE";
    private static final String DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUTS_PANCAKE = "DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUTS_PANCAKE";
    private static final String DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE = "DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE";
    private static final String MILK_CHOCOLATE_HAZELNUTS_PANCAKE = "MILK_CHOCOLATE_HAZELNUTS_PANCAKE";
    private static final String MILK_CHOCOLATE_PANCAKE = "MILK_CHOCOLATE_PANCAKE";

    private static final Map<String, List<Ingredient>> PANCAKES_MENU = new HashMap<>();

    static {
        PANCAKES_MENU.put(DARK_CHOCOLATE_PANCAKE, List.of(Ingredient.DARK_CHOCOLATE));
        PANCAKES_MENU.put(DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUTS_PANCAKE, List.of(
                Ingredient.DARK_CHOCOLATE,
                Ingredient.WHIPPED_CREAM,
                Ingredient.HAZELNUTS));
        PANCAKES_MENU.put(DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE, List.of(
                Ingredient.DARK_CHOCOLATE,
                Ingredient.WHIPPED_CREAM));
        PANCAKES_MENU.put(MILK_CHOCOLATE_HAZELNUTS_PANCAKE, List.of(
                Ingredient.MILK_CHOCOLATE,
                Ingredient.HAZELNUTS));
        PANCAKES_MENU.put(MILK_CHOCOLATE_PANCAKE, List.of(Ingredient.MILK_CHOCOLATE));
    }

    private PancakeFactory() {
    }

    private static PancakeRecipe createPancakeFromMenu(String name) {
        var ingredients = PANCAKES_MENU.get(name);
        if (ingredients == null) {
            throw new PancakeNotFoundException("Pancake " + name + " not found");
        }
        return new DojoPancake(ingredients);
    }

    public static PancakeRecipe createDarkChocolatePancake() {
        return createPancakeFromMenu(DARK_CHOCOLATE_PANCAKE);
    }

    public static PancakeRecipe createDarkChocolateWhippedCreamHazelnutsPancake() {
        return createPancakeFromMenu(DARK_CHOCOLATE_WHIPPED_CREAM_HAZELNUTS_PANCAKE);
    }

    public static PancakeRecipe createDarkChocolateWhippedCreamPancake() {
        return createPancakeFromMenu(DARK_CHOCOLATE_WHIPPED_CREAM_PANCAKE);
    }

    public static PancakeRecipe createMilkChocolateHazelnutsPancake() {
        return createPancakeFromMenu(MILK_CHOCOLATE_HAZELNUTS_PANCAKE);
    }

    public static PancakeRecipe createMilkChocolatePancake() {
        return createPancakeFromMenu(MILK_CHOCOLATE_PANCAKE);
    }

    public static PancakeRecipe createCustomPancake(List<Ingredient> ingredients) {
        return new DojoPancake(ingredients);
    }
}
