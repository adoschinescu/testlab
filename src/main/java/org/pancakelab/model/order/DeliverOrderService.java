package org.pancakelab.model.order;

import org.pancakelab.model.order.exception.DeliverOrderException;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.persistence.Orders;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class DeliverOrderService implements DeliverOrderUseCase {

    private final Orders orders;

    public DeliverOrderService(Orders orders) {
        this.orders = requireNonNull(orders, "Orders must not be null.");
    }

    @Override
    public Order deliverOrder(UUID orderId) {
        var order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (order.getStatus() != Order.Status.PREPARED) {
            throw new DeliverOrderException("Cannot deliver order with id %s and status %s".formatted(orderId, order.getStatus()));
        }
        orders.updateStatus(order.getId(), Order.Status.DELIVERED);
        orders.remove(orderId);
        return order;
    }
}
