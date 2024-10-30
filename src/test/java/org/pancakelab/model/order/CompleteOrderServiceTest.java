package org.pancakelab.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.exception.CompleteOrderException;
import org.pancakelab.model.pancakes.DojoPancake;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

import java.util.List;

class CompleteOrderServiceTest {

    private final Orders orders = new ConcurrentOrderStore();
    private final CreateOrderUseCase createOrderUseCase = new CreateOrderService(orders);
    private final CompleteOrderUseCase completeOrderUseCase = new CompleteOrderService(orders);

    @Test
    void GivenOrderExists_WhenCompleteOrder_ThenOrderStatusIsSuccessfullyChanged_Test() {
        var order = createOrderUseCase.createOrder(85, 22);

        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        createOrderUseCase.addPancake(order.getId(), dojoPancake1);
        createOrderUseCase.addPancake(order.getId(), dojoPancake2);
        createOrderUseCase.addPancake(order.getId(), dojoPancake3);

        completeOrderUseCase.completeOrder(order.getId());

        Assertions.assertSame(Order.Status.COMPLETED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isPresent());
        Assertions.assertSame(order.getId(), dbOrder.get().getId());
        Assertions.assertEquals(order.getBuilding(), dbOrder.get().getBuilding());
        Assertions.assertEquals(order.getRoom(), dbOrder.get().getRoom());
        Assertions.assertSame(Order.Status.COMPLETED, dbOrder.get().getStatus());
        Assertions.assertEquals(order.getPancakes(), dbOrder.get().getPancakes());
    }

    @Test
    void GivenOrderExists_WhenCompleteOrderWithInvalidStatus_ThenOperationFails_Test() {
        var order = createOrderUseCase.createOrder(85, 22);
        order.setStatus(Order.Status.COMPLETED);

        Assertions.assertThrows(CompleteOrderException.class,
                () -> completeOrderUseCase.completeOrder(order.getId()));
    }
}