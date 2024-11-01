package org.pancakelab.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.pancakelab.model.order.Order;
import org.pancakelab.model.order.exception.OrderNotFoundException;
import org.pancakelab.model.pancakes.Ingredient;
import org.pancakelab.model.pancakes.PancakeFactory;
import org.pancakelab.persistence.ConcurrentOrderStore;
import org.pancakelab.persistence.Orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PancakeServiceTest {

    private static final int DISCIPLES_COUNT = 2000;
    private static final int DELIVERY_COUNT = 120;

    private final Orders concurrentOrders = new ConcurrentOrderStore();
    private final PancakeService pancakeService = new PancakeService(concurrentOrders);
    private Order order;
    private ExecutorService disciplesExecutorService;
    private ExecutorService senseiExecutorService;
    private ExecutorService deliveryExecutorService;

    private final static String DARK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious Pancake with Dark chocolate!";
    private final static String MILK_CHOCOLATE_PANCAKE_DESCRIPTION = "Delicious Pancake with Milk chocolate!";
    private final static String MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION = "Delicious Pancake with Milk chocolate, Hazelnuts!";
    private final static String MAPLE_SYRUP_VANILLA_PANCAKE_DESCRIPTION = "Delicious Pancake with Maple syrup, Vanilla!";

    @BeforeEach
    public void setUp() {
        order = pancakeService.createOrder(85, 22);

        disciplesExecutorService = Executors.newFixedThreadPool(DISCIPLES_COUNT);
        deliveryExecutorService = Executors.newFixedThreadPool(DELIVERY_COUNT);
        senseiExecutorService = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void tearDown() {
        senseiExecutorService.shutdownNow();
        disciplesExecutorService.shutdownNow();
        deliveryExecutorService.shutdownNow();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void GivenDisciplesAdjustOrdersInParallel_WhenUsingConcurrentOrderStore_ThenConcurrentModificationExceptionIsNotThrown_Test() {
        List<CompletableFuture<Order>> taskChains = new ArrayList<>();
        var canceledOrderCount = new AtomicInteger(0);
        var completedOrderCount = new AtomicInteger(0);

        for (int i = 0; i < DISCIPLES_COUNT; i++) {
            var taskChain = CompletableFuture.supplyAsync(() -> {
                        var order = pancakeService.createOrder(new Random().nextInt(40, 100), new Random().nextInt(15, 80));
                        int pancakesCount = new Random().nextInt(1, 15);
                        for (int j = 0; j < pancakesCount; j++) {
                            int ingredientsCount = new Random().nextInt(1, 5);
                            List<Ingredient> ingredients = new ArrayList<>();
                            for (int k = 0; k < ingredientsCount; k++) {
                                ingredients.add(Ingredient.values()[new Random().nextInt(Ingredient.values().length)]);
                            }
                            pancakeService.addPancake(order.getId(), PancakeFactory.createCustomPancake(ingredients));
                        }
                        return order;
                    }, disciplesExecutorService)
                    .thenApplyAsync(discipleOrder -> {
                        final boolean cancelOrder = new Random().nextBoolean();
                        if (cancelOrder) {
                            canceledOrderCount.incrementAndGet();
                        }
                        final boolean completeOrder = !cancelOrder && new Random().nextBoolean();
                        if (completeOrder) {
                            completedOrderCount.incrementAndGet();
                        }
                        if (cancelOrder) {
                            disciplesExecutorService.submit(() -> pancakeService.cancelOrder(discipleOrder.getId()));
                        } else if (completeOrder) {
                            disciplesExecutorService.submit(() -> pancakeService.completeOrder(discipleOrder.getId()));
                        }
                        return discipleOrder;
                    }, disciplesExecutorService);

            taskChain.join();
            taskChains.add(taskChain);
        }

        Assertions.assertEquals(DISCIPLES_COUNT, taskChains.size());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    public void GivenOrderDoesNotExist_WhenCreatingOrder_ThenOrderCreatedWithCorrectData_Test() {
        // setup

        // exercise
        var order = pancakeService.createOrder(80, 30);
        var dbOrder = pancakeService.findOrder(order.getId());

        assertEquals(80, order.getBuilding());
        assertEquals(30, order.getRoom());

        assertEquals(80, dbOrder.getBuilding());
        assertEquals(30, dbOrder.getRoom());

        // verify

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    public void GivenOrderExists_WhenAddingPancakes_ThenCorrectNumberOfPancakesAdded_Test() {
        // setup

        // exercise
        addPancakes();

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MAPLE_SYRUP_VANILLA_PANCAKE_DESCRIPTION,
                MAPLE_SYRUP_VANILLA_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(30)
    public void GivenPancakesExists_WhenRemovingPancakes_ThenCorrectNumberOfPancakesRemoved_Test() {
        // setup
        addPancakes();

        // exercise
        pancakeService.removePancakes(DARK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 2);
        pancakeService.removePancakes(MILK_CHOCOLATE_PANCAKE_DESCRIPTION, order.getId(), 3);
        pancakeService.removePancakes(MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION, order.getId(), 1);
        pancakeService.removePancakes(MAPLE_SYRUP_VANILLA_PANCAKE_DESCRIPTION, order.getId(), 1);

        // verify
        List<String> ordersPancakes = pancakeService.viewOrder(order.getId());

        assertEquals(List.of(DARK_CHOCOLATE_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MILK_CHOCOLATE_HAZELNUTS_PANCAKE_DESCRIPTION,
                MAPLE_SYRUP_VANILLA_PANCAKE_DESCRIPTION), ordersPancakes);

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(40)
    public void GivenOrderExists_WhenCompletingOrder_ThenOrderCompleted_Test() {
        // setup
        addPancakes();

        // exercise
        pancakeService.completeOrder(order.getId());

        // verify
        Set<UUID> completedOrdersOrders = pancakeService.listCompletedOrders();
        assertTrue(completedOrdersOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(50)
    public void GivenOrderExists_WhenPreparingOrder_ThenOrderPrepared_Test() {
        // setup
        addPancakes();
        concurrentOrders.updateStatus(order.getId(), Order.Status.COMPLETED);

        // exercise
        pancakeService.prepareOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertTrue(preparedOrders.contains(order.getId()));

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(60)
    public void GivenOrderExists_WhenDeliveringOrder_ThenCorrectOrderReturnedAndOrderRemovedFromTheDatabase_Test() {
        // setup
        concurrentOrders.updateStatus(order.getId(), Order.Status.PREPARED);

        // exercise
        pancakeService.deliverOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        Assertions.assertTrue(concurrentOrders.find(order.getId()).isEmpty());

        // tear down
    }

    @Test
    @org.junit.jupiter.api.Order(70)
    public void GivenOrderExists_WhenCancellingOrder_ThenOrderAndPancakesRemoved_Test() {
        // setup
        addPancakes();

        // exercise
        pancakeService.cancelOrder(order.getId());

        // verify
        Set<UUID> completedOrders = pancakeService.listCompletedOrders();
        assertFalse(completedOrders.contains(order.getId()));

        Set<UUID> preparedOrders = pancakeService.listPreparedOrders();
        assertFalse(preparedOrders.contains(order.getId()));

        Assertions.assertThrows(OrderNotFoundException.class,
                () -> pancakeService.viewOrder(order.getId()));

        // tear down
    }

    private void addPancakes() {
        pancakeService.addDarkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolatePancake(order.getId(), 3);
        pancakeService.addMilkChocolateHazelnutsPancake(order.getId(), 3);
        pancakeService.addCustomPancake(order.getId(), 2, List.of(Ingredient.MAPLE_SYRUP, Ingredient.VANILLA));
    }
}
