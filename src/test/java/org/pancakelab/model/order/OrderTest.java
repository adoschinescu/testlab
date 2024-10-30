package org.pancakelab.model.order;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.pancakelab.model.pancakes.DojoPancake;
import org.pancakelab.model.pancakes.Ingredient;

import java.util.List;

class OrderTest {

    @Test
    void GivenCreateOrder_WhenCreatingOrder_ThenOrderIsSuccessfullyCreated_Test() {
        var order = new Order(7, 22);

        Assertions.assertNotNull(order.getId());
        Assertions.assertEquals(7, order.getBuilding());
        Assertions.assertEquals(22, order.getRoom());
        Assertions.assertEquals(Order.Status.CREATED, order.getStatus());
        Assertions.assertNotNull(order.getPancakes());
        Assertions.assertEquals(0, order.getPancakes().size());
    }

    @ParameterizedTest
    @EnumSource(Order.Status.class)
    void GivenOrderExists_WhenSetStatus_ThenStatusIsCorrectlySet_Test(Order.Status status) {
        var order = new Order(10, 22);

        order.setStatus(status);

        Assertions.assertEquals(status, order.getStatus());
    }

    @Test
    void GivenOrderExists_WhenSetStatusToNull_ThenOperationFails_Test() {
        var order = new Order(10, 22);

        Assertions.assertThrows(NullPointerException.class,
                () -> order.setStatus(null));
    }

    @Test
    void GivenOrderExists_WhenAddPancakes_ThenPancakeIsSuccessfullyAdded_Test() {
        var order = new Order(10, 22);
        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));

        order.addPancake(dojoPancake1);
        order.addPancake(dojoPancake2);

        Assertions.assertEquals(2, order.getPancakes().size());
        Assertions.assertEquals(dojoPancake1, order.getPancakes().get(0));
        Assertions.assertEquals(dojoPancake2, order.getPancakes().get(1));
    }

    @Test
    void GivenOrderExists_WhenRemovePancakes_ThenPancakeIsSuccessfullyRemoved_Test() {
        var order = new Order(10, 22);
        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        order.addPancake(dojoPancake1);
        order.addPancake(dojoPancake2);
        order.addPancake(dojoPancake3);

        order.removePancake(dojoPancake1);
        order.removePancake(dojoPancake3);

        Assertions.assertEquals(1, order.getPancakes().size());
        Assertions.assertEquals(dojoPancake2, order.getPancakes().get(0));
    }

    @Test
    void GivenOrderExists_WhenGetPancakes_ThenPancakesAreSuccessfullyRetrieved_Test() {
        var order = new Order(10, 22);
        var dojoPancake1 = new DojoPancake(List.of(Ingredient.DARK_CHOCOLATE, Ingredient.HAZELNUTS));
        var dojoPancake2 = new DojoPancake(List.of(Ingredient.MAPLE_SYRUP));
        var dojoPancake3 = new DojoPancake(List.of(Ingredient.WHIPPED_CREAM, Ingredient.VANILLA));
        order.addPancake(dojoPancake1);
        order.addPancake(dojoPancake2);
        order.addPancake(dojoPancake3);

        Assertions.assertEquals(List.of(dojoPancake1, dojoPancake2, dojoPancake3),
                order.getPancakes());
    }

    @Test
    void equalsHashCodeContract() {
        EqualsVerifier.forClass(Order.class)
                .withOnlyTheseFields("id")
                .verify();
    }
}