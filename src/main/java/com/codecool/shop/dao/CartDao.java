package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.util.List;

public interface CartDao {

    void add(Product product, int orderId);
    Cart find(int orderId);
    int getCount(int orderId);
    void removeByOrderId(int orderId);

}

