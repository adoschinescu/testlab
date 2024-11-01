package org.pancakelab.model.order;

import org.pancakelab.model.order.validation.DefaultDestinationValidator;
import org.pancakelab.model.pancakes.Recipe;
import org.pancakelab.persistence.Orders;

import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class CreateOrderService implements CreateOrderUseCase {

    private final Orders orders;
    private final DefaultDestinationValidator destinationValidator;

    public CreateOrderService(Orders orders) {
        this.orders = requireNonNull(orders, "Orders must not be null.");
        this.destinationValidator = new DefaultDestinationValidator();
    }

    @Override
    public Order createOrder(int building, int room) {
        destinationValidator.validate(building, room);
        var order = new Order(building, room);
        orders.add(order);
        return order;
    }

    @Override
    public Order addPancake(UUID orderId, Recipe pancake) {
        return orders.addPancake(orderId, pancake);
    }

    @Override
    public void removePancake(UUID orderId, Recipe pancake) {
        orders.removePancake(orderId, pancake);
    }

    public void removePancake(UUID orderId, Predicate<Recipe> filter) {
        orders.removePancake(orderId, filter);
    }
}
