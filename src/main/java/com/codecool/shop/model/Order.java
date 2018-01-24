package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class serves as model for every productList class and holds all the information about each of them.
 * Id field is an int it help to identify a concrete productList.
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
    private Map<Product, Integer> productList;
    private User user;
    private String status;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    /**
     * The constructor of the Order class
     * @param user the department of the product category
     */
    public Order(User user) {
        this.productList = new LinkedHashMap<>();
        this.user = user;
        logger.trace("Created instance of class Order with productList {} and username {}", productList, user);

    }

    /**
     * Gets the data of user linked to this productList
     * @return the data of the user linked to this productList
     */
    public HashMap getUserData() {
        logger.trace("User's data of this productList are {}", user.getUserData() );
        return user.getUserData();
    }

    /**
     * Gets the products and ids of products of this productList
     * @return the products and ids of products of this productList
     */
    public Map<Product, Integer> getProductList() {
        logger.trace("Products of this productList are {}", productList);
        return productList;
    }

    /**
     * Gets the id of this productList
     * @return the id of this productList
     */
    public int getId() {
        logger.trace("Id of this productList is {}", id );
        return id;
    }

    /**
     * Sets productList id
     * @param id what productList id should be set
     */
    public void setId(int id) {
        logger.trace("Set productList id {} to {}", this.id, id);
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
                ", productList=" + productList +
                ", userData=" + user.getUserData() +
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
        if (getProductList() != null ? !getProductList().equals(order1.getProductList()) : order1.getProductList() != null) return false;
        return getUserData() != null ? getUserData().equals(order1.getUserData()) : order1.getUserData() == null;
    }

    /**
     * Override the custom hashCode() method of the class
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getProductList() != null ? getProductList().hashCode() : 0);
        result = 31 * result + (getUserData() != null ? getUserData().hashCode() : 0);
        return result;
    }
}


