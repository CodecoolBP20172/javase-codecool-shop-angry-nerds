package com.codecool.shop.model;

import com.sun.tools.corba.se.idl.constExpr.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * This class serves as model for every cart class and holds all the information about each of them.
 * Id field is an int it help to identify a concrete cart.
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
    private Cart cart;
    private User user;
    private Status status;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    /**
     * The constructor of the Order class
     * @param user the department of the product category
     */
    public Order(User user) {
        this.cart = new Cart(id);
        this.user = user;
        this.status = Status.IN_CART;
        logger.trace("Created instance of class Order with username {}", user);

    }

    public Order(int id, User user, Cart cart, Status status) {
        this.id = id;
        this.user = user;
        this.cart = cart;
        this.status = status;
        logger.trace("Created instance of class Order with cart {} and username {}", cart, user);
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Gets the data of user linked to this cart
     * @return the data of the user linked to this cart
     */
    public HashMap getUserData() {
        logger.trace("User's data of this cart are {}", user.getUserData() );
        return user.getUserData();
    }

    public User getUser() {
        return user;
    }

    /**
     * Gets the products and ids of products of this cart
     * @return the products and ids of products of this cart
     */
    public Cart getCart() {
        logger.trace("Products of this cart are {}", cart);
        return cart;
    }

    /**
     * Gets the id of this cart
     * @return the id of this cart
     */
    public int getId() {
        logger.trace("Id of this cart is {}", id );
        return id;
    }

    /**
     * Sets cart id
     * @param id what cart id should be set
     */
    public void setId(int id) {
        logger.trace("Set cart id {} to {}", this.id, id);
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
                ", cart=" + cart +
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
        if (getCart() != null ? !getCart().equals(order1.getCart()) : order1.getCart() != null) return false;
        return getUserData() != null ? getUserData().equals(order1.getUserData()) : order1.getUserData() == null;
    }

    /**
     * Override the custom hashCode() method of the class
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getCart() != null ? getCart().hashCode() : 0);
        result = 31 * result + (getUserData() != null ? getUserData().hashCode() : 0);
        return result;
    }
}


