package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
