package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.database.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * This is a simple order data access object which serves the Order class and helpes him to
 * communicate with the database.
 * <p>
 * It implements the OrderDao interface. It has 3 methods, one, that adds an Order to the database,
 * one, that returns all orders in a list and one for returning the last order in the database.
 *
 * @author  Zsuzsa Petho
 * @version 1.0
 * @since   2018-01-20
 */
public class OrderDaoJDBC implements OrderDao{

    private static OrderDaoJDBC instance = null;
    private static CartDao cartData = CartDaoJDBC.getInstance();
    private static UserJDBC userDataJDBC = UserJDBC.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoJDBC.class);
    private static int CurrentOrderId;

    private OrderDaoJDBC() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        logger.debug("OrderDaoJDBC instance returning");
        return instance;
    }

    public static int getCurrentOrderId() {
        return CurrentOrderId;
    }

    public static void setCurrentOrderId(int currentOrderId) {
        CurrentOrderId = currentOrderId;
    }

    /**
     * This method adds an Order to the database. It has one parameter, which is an
     * Order that gets inserted into the database. If the argument is null it throws
     * an IllegalArgumentException.
     * @param order this is the Order that gets added to the DB
     * @throws IllegalArgumentException if the parameter order equals to null
     */
    @Override
    public void add(Order order) {
        if (order == null) {
            logger.debug("OrderDaoJDBC add method received invalid argument");
            throw new IllegalArgumentException();
        }
        String query = "INSERT INTO orders (user_data_id, status) VALUES (?, ?);";
        try(ConnectionHandler handler = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(order.getUser().getId()), true));
            queryList.add(new TypeCaster(String.valueOf(order.getStatus()), false));
            handler.execute(query, queryList);
            logger.debug("Order added successfully to database");
        } catch (SQLException e){
            e.printStackTrace();
            logger.warn("Connection to database failed while adding order to database");
        }
    }

    public List<Order> findByUserId(int userId) {
        String query = "SELECT * FROM orders WHERE user_data_id = ?;";
        List<Order> listOfOrders = new ArrayList<>();
        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(String.valueOf(userId), true));
            ResultSet rs = conn.process(query, list);
            while (rs.next()) {
                Integer orderId = rs.getInt("id");
                String status = rs.getString("status");
                Order order = new Order(orderId, userDataJDBC.getUser(userId),cartData.find(orderId),Status.valueOf(status));
                listOfOrders.add(order);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find product in database");
        }
        if (listOfOrders.get(0) != null) {
            logger.debug("Orders found in memory and returned");
            return listOfOrders;
        }
        else {
            logger.warn("No order found in database for this id");
            return null;
        }
    }

    public Order findByUserIdAndStatus(int userId, Status status) {
        List<Order> listOfOrders = findByUserId(userId);
        for (Order order : listOfOrders) {
            if (order.getStatus().equals(status)) return order;
        }
        return null;
    }
}
