package org.pancakelab.persistence;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.model.pancakes.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class NonConcurrentOrderStore implements Orders {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public void add(Order order) {
        orders.add(order);
    }

    @Override
    public void updateStatus(UUID orderId, Order.Status status) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
    }

    @Override
    public Order addPancake(UUID orderId, Recipe pancake) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.addPancake(pancake);
        return order;
    }

    @Override
    public void removePancake(UUID orderId, Recipe pancake) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.removePancake(pancake);
    }

    @Override
    public void removePancake(UUID orderId, Predicate<Recipe> filter) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.removePancake(filter);
    }

    @Override
    public void remove(UUID orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
    }

    @Override
    public Optional<Order> find(UUID orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    @Override
    public List<Order> listCompletedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == Order.Status.COMPLETED)
                .toList();
    }

    @Override
    public List<Order> listPreparedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == Order.Status.PREPARED)
                .toList();
    }
}
