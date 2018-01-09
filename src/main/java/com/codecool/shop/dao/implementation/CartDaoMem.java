package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class CartDaoMem implements CartDao {

    private ConcurrentMap<Product, AtomicLong> DATA= new ConcurrentHashMap<Product, AtomicLong>();
    private static CartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    CartDaoMem() {
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }
        if(DATA.containsKey(product)) {
            DATA.get(product).incrementAndGet();
        }
        else {
            DATA.put(product, new AtomicLong(1));
        }
    }

    @Override
    public Map<Product, Integer> getAll() {
        Map<Product, Integer> formedData = new HashMap<>();
        for (Map.Entry<Product, AtomicLong> entry : DATA.entrySet()) {
            formedData.put(entry.getKey(), entry.getValue().intValue());
        }
        return formedData;
    }


    @Override
    public String toString() {
        List<String> print = new ArrayList<>();
        for (Product name: DATA.keySet()){

            String key = name.getName();
            String value = DATA.get(name).toString();
            print.add(key + " : " + value);

        }
        return print.toString();
    }
    @Override
    public void remove(int id) {
        for (Map.Entry<Product, AtomicLong> entry : DATA.entrySet()) {
            if (entry.getKey().getId() == id) {
                DATA.remove(entry.getKey());
            }
        }
    }
    @Override
    public int getCount() {
        int count = 0;
        for (AtomicLong itemCount : DATA.values()) {
            count += itemCount.intValue();
        }
        return count;
    }
    @Override
    public void clearCart() {
        DATA.clear();
    }

    @Override
    public void setQuantity(int id, int quantity) {
        for (Map.Entry<Product, AtomicLong> entry : DATA.entrySet()) {
            if (entry.getKey().getId() == id) {
                DATA.put(entry.getKey(), new AtomicLong(quantity));
            }
        }
    }
}
