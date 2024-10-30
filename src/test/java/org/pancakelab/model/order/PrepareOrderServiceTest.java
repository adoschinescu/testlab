package org.pancakelab.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.exception.PrepareOrderException;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

class PrepareOrderServiceTest {

    private final Orders orders = new ConcurrentOrderStore();
    private final CreateOrderUseCase createOrderUseCase = new CreateOrderService(orders);
    private final PrepareOrderUseCase prepareOrderUseCase = new PrepareOrderService(orders);

    @Test
    void GivenOrderExists_WhenPrepareOrder_ThenOrderStatusIsSuccessfullyChanged_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.COMPLETED);

        prepareOrderUseCase.prepareOrder(order.getId());

        Assertions.assertSame(Order.Status.PREPARED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isPresent());
        Assertions.assertSame(order.getId(), dbOrder.get().getId());
        Assertions.assertEquals(order.getBuilding(), dbOrder.get().getBuilding());
        Assertions.assertEquals(order.getRoom(), dbOrder.get().getRoom());
        Assertions.assertSame(Order.Status.PREPARED, dbOrder.get().getStatus());
        Assertions.assertEquals(order.getPancakes(), dbOrder.get().getPancakes());
    }

    @Test
    void GivenOrderExists_WhenPrepareOrderWithInvalidStatus_ThenOperationFails_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.CREATED);

        Assertions.assertThrows(PrepareOrderException.class,
                () -> prepareOrderUseCase.prepareOrder(order.getId()));
    }
}