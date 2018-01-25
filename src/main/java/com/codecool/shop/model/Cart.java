package com.codecool.shop.model;

import java.util.LinkedHashMap;

public class Cart {
    private int orderId;
    private LinkedHashMap<Product, Integer> cart;

    public Cart(int orderId, LinkedHashMap<Product, Integer> cart) {
        this.orderId = orderId;
        this.cart = cart;
    }

    public Cart(int orderId) {
        this.orderId = orderId;
        cart = new LinkedHashMap<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LinkedHashMap<Product, Integer> getCart() {
        return cart;
    }

    public void setCart(LinkedHashMap<Product, Integer> cart) {
        this.cart = cart;
    }

    public void add(Product product) {
        if (cart.containsKey(product)) cart.put(product, cart.get(product) + 1);
        else cart.put(product, 1);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "orderId=" + orderId +
                ", cart=" + cart +
                '}';
    }
}
