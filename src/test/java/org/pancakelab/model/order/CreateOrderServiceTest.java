package org.pancakelab.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.validation.exception.BuildingNotFoundException;
import org.pancakelab.model.order.validation.exception.RoomNotFoundException;
import org.pancakelab.model.pancakes.DojoPancake;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

import java.util.List;

class CreateOrderServiceTest {

    private final Orders orders = new ConcurrentOrderStore();
    private final CreateOrderUseCase createOrderUseCase = new CreateOrderService(orders);

    @Test
    void GivenOrderExists_WhenCreateOrder_ThenOrderIsCreatedAndAddedToDatabase_Test() {
        var order = createOrderUseCase.createOrder(85, 22);

        Assertions.assertSame(Order.Status.CREATED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isPresent());
        Assertions.assertEquals(order, dbOrder.get());
    }

    @Test
    void GivenOrderExists_WhenAddPancakes_ThenPancakesAreSuccessfullyAddedToTheOrder_Test() {
        var order = createOrderUseCase.createOrder(85, 22);

        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        createOrderUseCase.addPancake(order.getId(), dojoPancake1);
        createOrderUseCase.addPancake(order.getId(), dojoPancake2);
        createOrderUseCase.addPancake(order.getId(), dojoPancake3);

        Assertions.assertSame(Order.Status.CREATED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isPresent());
        Assertions.assertEquals(order, dbOrder.get());
    }

    @Test
    void GivenOrderExists_WhenRemovePancakes_ThenPancakesAreSuccessfullyRemovedFromTheOrder_Test() {
        var order = createOrderUseCase.createOrder(85, 22);

        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        createOrderUseCase.addPancake(order.getId(), dojoPancake1);
        createOrderUseCase.addPancake(order.getId(), dojoPancake2);
        createOrderUseCase.addPancake(order.getId(), dojoPancake3);
        createOrderUseCase.removePancake(order.getId(), dojoPancake1);
        createOrderUseCase.removePancake(order.getId(), pancake -> pancake.description().equals(dojoPancake3.description()));

        Assertions.assertSame(Order.Status.CREATED, order.getStatus());
        var dbOrder = orders.find(order.getId());
        Assertions.assertTrue(dbOrder.isPresent());
        Assertions.assertEquals(1, dbOrder.get().getPancakes().size());
        Assertions.assertEquals(dojoPancake2, dbOrder.get().getPancakes().get(0));
    }

    @Test
    void GivenCreateOrder_WhenInvalidBuilding_ThenOperationFails_Test() {
        Assertions.assertThrows(BuildingNotFoundException.class,
                () -> createOrderUseCase.createOrder(39, 2));
        Assertions.assertThrows(BuildingNotFoundException.class,
                () -> createOrderUseCase.createOrder(101, 2));
    }

    @Test
    void GivenCreateOrder_WhenInvalidRoom_ThenOperationFails_Test() {
        Assertions.assertThrows(RoomNotFoundException.class,
                () -> createOrderUseCase.createOrder(50, 13));
        Assertions.assertThrows(RoomNotFoundException.class,
                () -> createOrderUseCase.createOrder(40, -1));
        Assertions.assertThrows(RoomNotFoundException.class,
                () -> createOrderUseCase.createOrder(40, 101));
    }
}