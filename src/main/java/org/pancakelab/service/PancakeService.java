package org.pancakelab.service;

import org.pancakelab.model.order.CancelOrderService;
import org.pancakelab.model.order.CancelOrderUseCase;
import org.pancakelab.model.order.CompleteOrderService;
import org.pancakelab.model.order.CompleteOrderUseCase;
import org.pancakelab.model.order.CreateOrderService;
import org.pancakelab.model.order.CreateOrderUseCase;
import org.pancakelab.model.order.DeliverOrderService;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.PrepareOrderService;
import org.pancakelab.model.order.PrepareOrderUseCase;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.model.pancakes.Recipe;
import org.pancakelab.persistence.Orders;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PancakeService {

    private final Orders orders;
    private final CreateOrderUseCase createOrderUseCase;
    private final CompleteOrderUseCase completeOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final PrepareOrderUseCase prepareOrderUseCase;
    private final DeliverOrderService deliverOrderService;

    public PancakeService(Orders orders) {
        this.orders = orders;
        this.createOrderUseCase = new CreateOrderService(orders);
        this.completeOrderUseCase = new CompleteOrderService(orders);
        this.cancelOrderUseCase = new CancelOrderService(orders);
        this.prepareOrderUseCase = new PrepareOrderService(orders);
        this.deliverOrderService = new DeliverOrderService(orders);
    }

    public Order createOrder(int building, int room) {
        return createOrderUseCase.createOrder(building, room);
    }

    public Order findOrder(UUID orderId) {
        return orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createDarkChocolatePancake());
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createDarkChocolateWhippedCreamPancake());
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createDarkChocolateWhippedCreamHazelnutsPancake());
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createMilkChocolatePancake());
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createMilkChocolateHazelnutsPancake());
        }
    }

    public void addCustomPancake(UUID orderId, int count, List<Ingredient> ingredients) {
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        for (int i = 0; i < count; i++) {
            addPancake(order.getId(), PancakeFactory.createCustomPancake(ingredients));
        }
    }

    public List<String> viewOrder(UUID orderId) {
        return orders.find(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"))
                .getPancakes().stream()
                .map(Recipe::description)
                .toList();
    }

    public void addPancake(UUID orderId, Recipe pancake) {
        var order = createOrderUseCase.addPancake(orderId, pancake);

        OrderLog.logAddPancake(order, pancake.description());
    }

    public void removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        Order order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        createOrderUseCase.removePancake(orderId, recipe -> recipe.description().equals(description) && removedCount.getAndIncrement() < count);

        OrderLog.logRemovePancakes(order, description, removedCount.get());
    }

    public void cancelOrder(UUID orderId) {
        Order order = cancelOrderUseCase.cancelOrder(orderId);

        OrderLog.logCancelOrder(order);
    }

    public void completeOrder(UUID orderId) {
        Order order = completeOrderUseCase.completeOrder(orderId);

        OrderLog.logCompleteOrder(order);
    }

    public Set<UUID> listCompletedOrders() {
        return orders.listCompletedOrders().stream()
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    public void prepareOrder(UUID orderId) {
        Order order = prepareOrderUseCase.prepareOrder(orderId);

        OrderLog.logPrepareOrder(order);
    }

    public Set<UUID> listPreparedOrders() {
        return orders.listPreparedOrders().stream()
                .map(Order::getId)
                .collect(Collectors.toSet());
    }

    public void deliverOrder(UUID orderId) {
        Order order = deliverOrderService.deliverOrder(orderId);

        OrderLog.logDeliverOrder(order);
    }
}
