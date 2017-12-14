package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface CartDao {

    void add(Product product);

    Map<Product, Integer> getAll();

    int getCount();
    void remove(int id);
    void setQuantity(int id, int quantity);
}

