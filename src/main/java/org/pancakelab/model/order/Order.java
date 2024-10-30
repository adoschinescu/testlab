package org.pancakelab.model.order;

import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.Recipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public final class Order {

    private final UUID id;
    private final int building;
    private final int room;
    private Status status;
    private final List<Recipe> pancakes;

    public Order(int building, int room) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.room = room;
        this.status = Status.CREATED;
        this.pancakes = new CopyOnWriteArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = Objects.requireNonNull(status);
    }

    public void addPancake(PancakeRecipe pancake) {
        pancakes.add(pancake);
    }

    public void removePancake(PancakeRecipe pancake) {
        pancakes.removeIf(pancakeRecipe -> pancake.description().equals(pancakeRecipe.description()));
    }

    public void removePancake(Predicate<Recipe> filter) {
        pancakes.removeIf(filter);
    }

    public List<Recipe> getPancakes() {
        return Collections.unmodifiableList(pancakes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public enum Status {
        CREATED, COMPLETED, CANCELLED, PREPARED, DELIVERED
    }
}
