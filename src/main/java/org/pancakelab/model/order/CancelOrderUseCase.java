package org.pancakelab.model.order;

import java.util.UUID;

public interface CancelOrderUseCase {

    Order cancelOrder(UUID orderId);
}
