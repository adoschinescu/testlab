package org.pancakelab.model.order.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pancakelab.model.order.validation.exception.BuildingNotFoundException;
import org.pancakelab.model.order.validation.exception.RoomNotFoundException;

class DefaultDestinationValidatorTest {

    private final DestinationValidator destinationValidator = new DefaultDestinationValidator();

    @Test
    void GivenBuildingDoesNotExist_WhenValidatingBuilding_ThenValidationFails_Test() {
        Assertions.assertThrows(BuildingNotFoundException.class,
                () -> destinationValidator.validate(39, 2));

        Assertions.assertThrows(BuildingNotFoundException.class,
                () -> destinationValidator.validate(101, 2));
    }

    @Test
    void GivenBuildingExists_WhenValidatingBuilding_ThenValidationPasses_Test() {
        Assertions.assertDoesNotThrow(() -> destinationValidator.validate(40, 2));
        Assertions.assertDoesNotThrow(() -> destinationValidator.validate(80, 2));
        Assertions.assertDoesNotThrow(() -> destinationValidator.validate(100, 2));
    }

    @Test
    void GivenRoomDoesNotExist_WhenValidatingRoom_ThenValidationFails_Test() {
        Assertions.assertThrows(RoomNotFoundException.class,
                () -> destinationValidator.validate(50, 13));

        Assertions.assertThrows(RoomNotFoundException.class,
                () -> destinationValidator.validate(40, -1));

        Assertions.assertThrows(RoomNotFoundException.class,
                () -> destinationValidator.validate(40, 101));
    }

    @Test
    void GivenRoomExists_WhenValidatingRoom_ThenValidationPasses_Test() {
        Assertions.assertDoesNotThrow(() -> destinationValidator.validate(80, 90));
    }
}