package com.codecool.shop.dao;

import com.codecool.shop.model.*;

import java.util.List;

public interface OrderDao {

    public void add(Order order, int userId);

    public void changeStatus(int orderId, Status status);

    public List<Order> findByUserId(int userId);

    public Order findByUserIdAndStatus(int userId, Status status);

}
