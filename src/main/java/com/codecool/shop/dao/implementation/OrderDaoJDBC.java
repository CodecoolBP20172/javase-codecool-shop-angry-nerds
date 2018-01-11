package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        String query = "INSERT INTO orders VALUES(1, 1);";
        try(ConnectionHandler handler = new ConnectionHandler()) {
            handler.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList <>();
        String query = "SELECT * orders";
        try (ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userDataId = rs.getInt("user_data_id");
                int cartId = rs.getInt("cart_id");
                orders.add(new Order(CartDaoJDBC.getInstance.getAll(), UserJDBC.getUser(userDataId).getUserData())); //CartDao needs a get
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getLast() {
        Order lastOrder = null;
        String query = "SELECT * orders ORDER BY id DESC limit 1";
        try(ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query);
            rs.next();
            int id = rs.getInt("id");
            int userDataId = rs.getInt("user_data_id");
            int cartId = rs.getInt("cart_id");
            lastOrder = new Order(CartDaoJDBC.getInstance.getAll(), UserJDBC.getUser(userDataId).getUserData());
        } catch (SQLException e){
            e.printStackTrace();
        }
        return lastOrder;
    }
}
