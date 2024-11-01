package org.pancakelab.service;

import org.pancakelab.model.order.Order;

import java.util.logging.Logger;

public class OrderLog {

    private static final Logger log = Logger.getLogger(OrderLog.class.getName());

    public static void logAddPancake(Order order, String description) {
        long pancakesInOrder = order.getPancakes().size();

        log.info("Added pancake with description '%s' ".formatted(description)
                + "to order %s containing %d pancakes, ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public static void logRemovePancakes(Order order, String description, int count) {
        long pancakesInOrder = order.getPancakes().size();

        log.info("Removed %d pancake(s) with description '%s' ".formatted(count, description)
                + "from order %s now containing %d pancakes, ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public static void logCompleteOrder(Order order) {
        long pancakesInOrder = order.getPancakes().size();
        log.info("Completed order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public static void logCancelOrder(Order order) {
        long pancakesInOrder = order.getPancakes().size();
        log.info("Cancelled order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public static void logPrepareOrder(Order order) {
        long pancakesInOrder = order.getPancakes().size();
        log.info("Prepared order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
    }

    public static void logDeliverOrder(Order order) {
        long pancakesInOrder = order.getPancakes().size();
        log.info("Order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)
                + "for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom()));
    }
}
