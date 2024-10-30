package org.pancakelab.model.order;

import java.util.UUID;

public interface PrepareOrderUseCase {

    Order prepareOrder(UUID orderId);
}
