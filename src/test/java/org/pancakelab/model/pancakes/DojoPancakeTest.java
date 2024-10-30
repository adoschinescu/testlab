package org.pancakelab.model.pancakes;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DojoPancakeTest {

    @Test
    void GivenCreateDojoPancake_WhenIngredientsAreNull_ThenOperationFails_Test() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new DojoPancake(null));
    }

    @Test
    void GivenCreateDojoPancake_WhenRetrievingName_ThenNameIsPancake_Test() {
        var dojoPancake = new DojoPancake(List.of());

        Assertions.assertEquals("Pancake", dojoPancake.name());
    }

    @Test
    void GivenCreateDojoPancake_WhenRetrievingDescription_ThenDescriptionIsCorrectlyConstructed_Test() {
        var dojoPancake = new DojoPancake(List.of(Ingredient.MILK_CHOCOLATE, Ingredient.HAZELNUTS));

        Assertions.assertEquals("Delicious Pancake with Milk chocolate, Hazelnuts!", dojoPancake.description());
    }

    @Test
    void equalsHashCodeContract() {
        EqualsVerifier.forClass(DojoPancake.class)
                .usingGetClass()
                .verify();
    }
}