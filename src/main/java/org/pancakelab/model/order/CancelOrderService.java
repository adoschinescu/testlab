package org.pancakelab.model.order;

import org.pancakelab.model.order.exception.CancelOrderException;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.persistence.Orders;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class CancelOrderService implements CancelOrderUseCase {

    private final Orders orders;

    public CancelOrderService(Orders orders) {
        this.orders = requireNonNull(orders, "Orders must not be null.");
    }

    @Override
    public synchronized Order cancelOrder(UUID orderId) {
        var order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (order.getStatus() != Order.Status.CREATED) {
            throw new CancelOrderException("Cannot cancel order with id %s and status %s".formatted(orderId, order.getStatus()));
        }
        orders.updateStatus(order.getId(), Order.Status.CANCELLED);
        orders.remove(orderId);
        return order;
    }
}
