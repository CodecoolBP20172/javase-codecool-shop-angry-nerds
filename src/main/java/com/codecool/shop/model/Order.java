package com.codecool.shop.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private int id;
    private Map<Product, Integer> order = new HashMap<Product, Integer>();
    private LinkedHashMap userData = new LinkedHashMap();

    public Order(Map<Product, Integer> order, LinkedHashMap userData) {
        this.order = order;
        this.userData = userData;
    }

    public HashMap getUserData() {
        return userData;
    }

    public Map<Product, Integer> getOrder() {
        return order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order=" + order +
                ", userData=" + userData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order1 = (Order) o;

        if (getId() != order1.getId()) return false;
        if (getOrder() != null ? !getOrder().equals(order1.getOrder()) : order1.getOrder() != null) return false;
        return getUserData() != null ? getUserData().equals(order1.getUserData()) : order1.getUserData() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
        result = 31 * result + (getUserData() != null ? getUserData().hashCode() : 0);
        return result;
    }
}


