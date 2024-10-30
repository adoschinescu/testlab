package org.pancakelab.model.pancakes;

public interface PancakeRecipe extends Recipe {

    @Override
    default String name() {
        return "Pancake";
    }
}
