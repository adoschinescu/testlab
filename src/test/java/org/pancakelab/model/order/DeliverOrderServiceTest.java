package org.pancakelab.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.exception.DeliverOrderException;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

class DeliverOrderServiceTest {

    private final Orders orders = new ConcurrentOrderStore();
    private final CreateOrderUseCase createOrderUseCase = new CreateOrderService(orders);
    private final DeliverOrderService deliverOrderService = new DeliverOrderService(orders);

    @Test
    void GivenOrderExists_WhenDeliverOrder_ThenOrderStatusIsSuccessfullyChangedAndRemovedFromDatabase_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.PREPARED);

        deliverOrderService.deliverOrder(order.getId());

        Assertions.assertSame(Order.Status.DELIVERED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isEmpty());
    }

    @Test
    void GivenOrderExists_WhenDeliverOrderWithInvalidStatus_ThenOperationFails_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.CREATED);

        Assertions.assertThrows(DeliverOrderException.class,
                () -> deliverOrderService.deliverOrder(order.getId()));
    }
}