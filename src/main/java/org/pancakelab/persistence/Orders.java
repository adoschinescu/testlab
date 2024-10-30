package org.pancakelab.persistence;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface Orders {

    void add(Order order);

    void updateStatus(UUID orderId, Order.Status status);

    Order addPancake(UUID orderId, PancakeRecipe pancake);

    void removePancake(UUID orderId, PancakeRecipe pancake);

    void removePancake(UUID orderId, Predicate<Recipe> filter);

    void remove(UUID orderId);

    Optional<Order> find(UUID orderId);

    List<Order> listCompletedOrders();

    List<Order> listPreparedOrders();
}
