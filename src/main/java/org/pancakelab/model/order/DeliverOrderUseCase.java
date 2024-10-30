package org.pancakelab.model.order;

import java.util.UUID;

public interface DeliverOrderUseCase {

    Order deliverOrder(UUID orderId);
}
