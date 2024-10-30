package org.pancakelab.model.order.validation;

import org.pancakelab.model.order.validation.exception.BuildingNotFoundException;
import org.pancakelab.model.order.validation.exception.RoomNotFoundException;

public final class DefaultDestinationValidator implements DestinationValidator {

    @Override
    public void validate(int building, int room) {
        if (building < 40 || building > 100) {
            throw new BuildingNotFoundException("Building not found");
        }
        if ((building == 50 && room == 13) || room < 0 || room > 100) {
            throw new RoomNotFoundException("Room not found");
        }
    }
}
