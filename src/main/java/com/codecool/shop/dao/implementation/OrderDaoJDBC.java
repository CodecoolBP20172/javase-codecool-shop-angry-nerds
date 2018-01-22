package com.codecool.shop.dao.implementation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoJDBC.class);

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
        String query = "INSERT INTO orders (user_data_id, cart_id) VALUES (1, 1);";
        try(ConnectionHandler handler = new ConnectionHandler()) {
            handler.execute(query);
            logger.debug("Order added successfully to database");
        } catch (SQLException e){
            e.printStackTrace();
            logger.warn("Connection to database failed while adding order to database");
        }
    }

    /**
     * This method gets all the orders from the database and returns them in a list.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the connection went through without problems,
     * it then returns all the orders in a List.
     * @return a List, which contains all the orders
     */
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList <>();
        String query = "SELECT * FROM orders;";
        try (ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userDataId = rs.getInt("user_data_id");
                int cartId = rs.getInt("cart_id");
                orders.add(new Order(CartDaoJDBC.getInstance().getAll(), UserJDBC.getUser(userDataId).getUserData())); //CartDao needs a get
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.warn("Connection to database failed while trying get all order from database");
        }
        logger.debug("Returning all orders in a list");
        return orders;
    }

    /**
     * This method gets the last order from the database and returns it.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the connection went through without problems,
     * then it returns the last order.
     * @return an Order, which is the last order in the database.
     */
    @Override
    public Order getLast() {
        Order lastOrder = null;
        String query = "SELECT * FROM orders ORDER BY id DESC limit 1;";
        try(ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query);
            rs.next();
            int id = rs.getInt("id");
            int userDataId = rs.getInt("user_data_id");
            int cartId = rs.getInt("cart_id");
            lastOrder = new Order(CartDaoJDBC.getInstance().getAll(), UserJDBC.getUser(userDataId).getUserData());
            lastOrder.setId(id);
        } catch (SQLException e){
            e.printStackTrace();
            logger.warn("Connection to database failed while trying get the last order from database");
        }
        logger.debug("Returning the last order in a list");
        return lastOrder;
    }
}
