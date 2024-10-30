package org.pancakelab.model.order;

import java.util.UUID;

public interface CompleteOrderUseCase {

    Order completeOrder(UUID orderId);
}
