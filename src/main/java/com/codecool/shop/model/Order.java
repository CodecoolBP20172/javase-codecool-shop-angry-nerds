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



}


