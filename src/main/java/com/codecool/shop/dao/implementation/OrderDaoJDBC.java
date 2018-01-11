package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;

import java.util.List;

public class OrderDaoJDBC implements OrderDao{

    private static OrderDaoJDBC instance = null;

    private OrderDaoJDBC() {
    }

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }


    @Override
    public void add(Order order) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public Order getLast() {
        return null;
    }
}
