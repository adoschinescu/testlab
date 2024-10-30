package org.pancakelab.model.pancakes;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DojoPancake implements PancakeRecipe {

    private final List<Ingredient> ingredients;

    public DojoPancake(List<Ingredient> ingredients) {
        this.ingredients = Objects.requireNonNull(ingredients);
    }

    @Override
    public List<Ingredient> ingredients() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DojoPancake that = (DojoPancake) o;
        return Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ingredients);
    }
}
