package org.pancakelab.model.order.validation;

public interface DestinationValidator {

    default void validate(int building, int room) {
        // nothing, all buildings and all rooms are permitted
    }
}
