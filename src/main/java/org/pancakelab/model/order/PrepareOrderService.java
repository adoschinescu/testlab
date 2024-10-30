package org.pancakelab.model.order;

import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.model.order.exception.PrepareOrderException;
import org.pancakelab.persistence.Orders;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public final class PrepareOrderService implements PrepareOrderUseCase {

    private final Orders orders;

    public PrepareOrderService(Orders orders) {
        this.orders = requireNonNull(orders, "Orders must not be null.");
    }

    @Override
    public Order prepareOrder(UUID orderId) {
        var order = orders.find(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (order.getStatus() != Order.Status.COMPLETED) {
            throw new PrepareOrderException("Cannot prepare order with id %s and status %s".formatted(orderId, order.getStatus()));
        }
        orders.updateStatus(order.getId(), Order.Status.PREPARED);
        return order;
    }
}
