package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class serves as model for every order class and holds all the information about each of them.
 * Id field is an int it help to identify a concrete order.
 * Order field is a HashMap which contains a content of a cart<Product, Integer>.
 * UserData field is a LinkedHashMap which contains the user data.
 *
 *
 * @author  Zsuzsa Petho
 * @version 1.0
 * @since   2018-01-20
 */

public class Order {
    private int id;
    private Map<Product, Integer> order = new HashMap<Product, Integer>();
    private LinkedHashMap userData = new LinkedHashMap();

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    /**
     * The constructor of the Order class
     * @param order content of a cart
     * @param userData the department of the product category
     */
    public Order(Map<Product, Integer> order, LinkedHashMap userData) {
        this.order = order;
        this.userData = userData;
        logger.trace("Created instance of class Order with order {} and userData {}", order, userData);

    }

    /**
     * Gets the data of user linked to this order
     * @return the data of the user linked to this order
     */
    public HashMap getUserData() {
        logger.trace("User's data of this order are {}", userData );
        return userData;
    }

    /**
     * Gets the products and ids of products of this order
     * @return the products and ids of products of this order
     */
    public Map<Product, Integer> getOrder() {
        logger.trace("Products of this order are {}", order );
        return order;
    }

    /**
     * Gets the id of this order
     * @return the id of this order
     */
    public int getId() {
        logger.trace("Id of this order is {}", id );
        return id;
    }

    /**
     * Sets order id
     * @param id what order id should be set
     */
    public void setId(int id) {
        logger.trace("Set order id {} to {}", this.id, id);
        this.id = id;
    }

    /**
     * Sets a custom toString representation of the class
     * @return the new string format of the Order class
     */
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order=" + order +
                ", userData=" + userData +
                '}';
    }

    /**
     * Override the custom equals method of the class
     * @param   o   the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order1 = (Order) o;

        if (getId() != order1.getId()) return false;
        if (getOrder() != null ? !getOrder().equals(order1.getOrder()) : order1.getOrder() != null) return false;
        return getUserData() != null ? getUserData().equals(order1.getUserData()) : order1.getUserData() == null;
    }

    /**
     * Override the custom hashCode() method of the class
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
        result = 31 * result + (getUserData() != null ? getUserData().hashCode() : 0);
        return result;
    }
}


