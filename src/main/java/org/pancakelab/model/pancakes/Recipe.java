package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.stream.Collectors;

public interface Recipe {

    default String description() {
        return "Delicious %s with %s!".formatted(name(),
                ingredients().stream()
                        .map(Ingredient::getDescription)
                        .collect(Collectors.joining(", "))
        );
    }

    String name();

    List<Ingredient> ingredients();
}
