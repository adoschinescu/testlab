package org.pancakelab.model.order.exception;

public class CancelOrderException extends RuntimeException {

    public CancelOrderException(String message) {
        super(message);
    }
}
