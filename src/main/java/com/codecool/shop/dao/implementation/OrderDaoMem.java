package com.codecool.shop.dao.implementation;

import com.codecool.shop.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import java.util.ArrayList;
import java.util.List;


/**
 * This is a simple order data access object which helps the Order class to save and load
 * data from the memory.
 * <p>
 * It implements the OrderDao interface. It has 3 methods, one, that adds an Order to the memory,
 * one, that returns all Orders in a List and one, that returns the last order in the memory.
 *
 * @author  Zsuzsa Petho
 * @version 1.0
 * @since   2018-01-20
 */
public class OrderDaoMem implements OrderDao {

    private List<Order> DATA = new ArrayList<>();
    private static OrderDaoMem instance = null;

    private static final Logger logger = LoggerFactory.getLogger(OrderDaoMem.class);

    private OrderDaoMem() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    /**
     * This method adds an Order to a List, which represents the memory. It has
     * one parameter, which is an Order that gets inserted into the list. If the argument
     * is null it throws an IllegalArgumentException.
     * @param order this is the Order that gets added to the memory
     * @throws IllegalArgumentException if the parameter category equals to null
     */
    @Override
    public void add(Order order) {
        if (order == null) {
            logger.debug("OrderDaoMem add method received invalid argument");
            throw new IllegalArgumentException();
        }
        order.setId(DATA.size() + 1);
        DATA.add(order);
        logger.debug("Order added successfully to the memory");
    }

    @Override
    public List<Order> findByUserId(int userId) {
        return null;
    }

    @Override
    public Order findByUserIdAndStatus(int userId, Status status) {
        return null;
    }

}
