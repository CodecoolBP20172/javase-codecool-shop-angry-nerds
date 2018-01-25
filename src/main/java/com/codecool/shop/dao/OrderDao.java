package com.codecool.shop.dao;

import com.codecool.shop.model.*;

import java.util.List;

public interface OrderDao {

    void add(Order order);

    public List<Order> findByUserId(int userId);

    public Order findByUserIdAndStatus(int userId, Status status);

}
