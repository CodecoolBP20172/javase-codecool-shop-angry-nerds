package com.codecool.shop.dao.implementation;


import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class CartDaoMem implements CartDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private Map<Product, Integer> DATA= new HashMap<Product, Integer>();
    private static CartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private CartDaoMem() {
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product, int orderId) {
        DATA.put(product, orderId);
    }

    @Override
    public Cart find(int orderId) {
        Cart cart = new Cart(orderId);
        for (Map.Entry<Product, Integer> data : DATA.entrySet()) {
            if (orderId == data.getValue()) cart.add(data.getKey());
        }
        return cart;
    }

    @Override
    public int getCount(int orderId) {
        Integer count = 0;
        for (Integer value : find(orderId).getCart().values()) {
            count += value;
        }
        logger.debug("Count value of cart: {}", count);
        return count;
    }

    @Override
    public void removeByOrderId(int orderId) {
        for (Map.Entry<Product, Integer> data : DATA.entrySet()) {
            if (data.getValue() == orderId) DATA.remove(data);
        }
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
}
