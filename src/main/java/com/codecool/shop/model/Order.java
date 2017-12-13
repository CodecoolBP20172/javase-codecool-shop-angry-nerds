package com.codecool.shop.model;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private int id;
    private ConcurrentMap<Product, AtomicLong> order = new ConcurrentHashMap<Product, AtomicLong>();
    private HashMap userData = new HashMap();

    public Order(ConcurrentMap<Product, AtomicLong> order, HashMap userData) {
        this.order = order;
        this.userData = userData;
    }

    public void setId(int id) {
        this.id = id;
    }

}


