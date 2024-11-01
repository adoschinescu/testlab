package org.pancakelab.model.order;

import org.pancakelab.model.order.exception.CompleteOrderException;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.persistence.Orders;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class CompleteOrderService implements CompleteOrderUseCase {

    private final Orders orders;

    public CompleteOrderService(Orders orders) {
        this.orders = requireNonNull(orders, "Orders must not be null.");
    }

    @Override
    public Order completeOrder(UUID orderId) {
        var order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (order.getStatus() != Order.Status.CREATED) {
            throw new CompleteOrderException("Cannot complete order with id %s and status %s".formatted(orderId, order.getStatus()));
        }
        if (order.getPancakes().isEmpty()) {
            throw new CompleteOrderException("Cannot complete order with id %s and no pancakes".formatted(orderId));
        }
        orders.updateStatus(order.getId(), Order.Status.COMPLETED);
        return order;
    }
}
