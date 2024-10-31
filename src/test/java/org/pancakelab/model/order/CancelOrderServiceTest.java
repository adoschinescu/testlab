package org.pancakelab.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.exception.CancelOrderException;
import org.pancakelab.model.pancakes.DojoPancake;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

import java.util.List;

class CancelOrderServiceTest {

    private final Orders orders = new ConcurrentOrderStore();
    private final CreateOrderUseCase createOrderUseCase = new CreateOrderService(orders);
    private final CancelOrderUseCase cancelOrderUseCase = new CancelOrderService(orders);

    @Test
    void GivenOrderExists_WhenCancelOrder_ThenOrderStatusIsSuccessfullyChangedAndRemovedFromDatabase_Test() {
        var order = createOrderUseCase.createOrder(85, 22);

        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        createOrderUseCase.addPancake(order.getId(), dojoPancake1);
        createOrderUseCase.addPancake(order.getId(), dojoPancake2);
        createOrderUseCase.addPancake(order.getId(), dojoPancake3);

        cancelOrderUseCase.cancelOrder(order.getId());
        Assertions.assertSame(Order.Status.CANCELLED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isEmpty());
    }

    @Test
    void GivenOrderExists_WhenCancelOrderWithInvalidStatus_ThenOperationFails_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.COMPLETED);

        Assertions.assertThrows(CancelOrderException.class,
                () -> cancelOrderUseCase.cancelOrder(order.getId()));
    }
}