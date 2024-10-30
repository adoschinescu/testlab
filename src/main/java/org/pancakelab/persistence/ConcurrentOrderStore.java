package org.pancakelab.persistence;

import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.Recipe;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class ConcurrentOrderStore implements Orders {

    private final List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void add(Order order) {
        orders.add(order);
    }

    @Override
    public synchronized void updateStatus(UUID orderId, Order.Status status) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(status);
    }

    @Override
    public synchronized Order addPancake(UUID orderId, PancakeRecipe pancake) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.addPancake(pancake);
        return order;
    }

    @Override
    public synchronized void removePancake(UUID orderId, PancakeRecipe pancake) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.removePancake(pancake);
    }

    @Override
    public synchronized void removePancake(UUID orderId, Predicate<Recipe> filter) {
        Order order = find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.removePancake(filter);
    }

    @Override
    public synchronized void remove(UUID orderId) {
        orders.removeIf(order -> order.getId().equals(orderId));
    }

    @Override
    public synchronized Optional<Order> find(UUID orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst();
    }

    @Override
    public synchronized List<Order> listCompletedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == Order.Status.COMPLETED)
                .toList();
    }

    @Override
    public synchronized List<Order> listPreparedOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == Order.Status.PREPARED)
                .toList();
    }
}
