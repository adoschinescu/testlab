package org.pancakelab.model.order;

import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.Recipe;

import java.util.UUID;
import java.util.function.Predicate;

public interface CreateOrderUseCase {

    Order createOrder(int building, int room);

    Order addPancake(UUID orderId, PancakeRecipe pancake);

    void removePancake(UUID orderId, PancakeRecipe pancake);

    void removePancake(UUID orderId, Predicate<Recipe> filter);
}