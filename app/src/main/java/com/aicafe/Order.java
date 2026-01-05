package com.aicafe;

import java.util.List;
import java.util.Objects;

public class Order {
    private final String id;
    private final List<MenuItem> items;
    private final double total;
    private final String date;

    public Order(String id, List<MenuItem> items, double total, String date) {
        this.id = id;
        this.items = items;
        this.total = total;
        this.date = date;
    }

    public String getId() { return id; }
    public List<MenuItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getDate() { return date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.total, total) == 0 &&
                id.equals(order.id) &&
                items.equals(order.items) &&
                date.equals(order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items, total, date);
    }
}
