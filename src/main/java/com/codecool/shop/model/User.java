package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class serves as model for every user class and holds all the information about each of them.
 * Id field is an int to identify a concrete user.
 * UserData field is a LinkedHashMap which contains the user data.
 *
 *
 * @author  Zsuzsa Petho
 * @version 1.0
 * @since   2018-01-20
 */

public class User{

    private LinkedHashMap userData;
    private int id;

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    /**
     * The constructor of the Order class
     * @param name name of the user
     * @param email email of the user
     * @param phone_number phone number of the user
     * @param billing_address billing address of the user
     * @param billing_city billing city of the user
     * @param billing_zipcode billing zipcode of the user
     * @param billing_country billing country of the user
     * @param shipping_address shipping address of the user
     * @param shipping_city shipping city of the user
     * @param shipping_zipcode shipping zipcode of the user
     * @param shipping_country shipping country of the user
     */
    public User(String name, String email, String phone_number, String billing_address, String billing_city,
                String billing_zipcode, String billing_country, String shipping_address, String shipping_city,
                String shipping_zipcode, String shipping_country){


        List<String> list = new ArrayList<>(Arrays.asList("name", "email", "Phone Number", "Billing Address",
                "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City",
                "Shipping Zipcode",  "Shipping Country"));
        List<String> data = new ArrayList<>(Arrays.asList(name, email, phone_number, billing_address, billing_city,
                billing_zipcode, billing_country,shipping_address, shipping_city, shipping_zipcode,  shipping_country));
        userData = new LinkedHashMap();
        for (int i=0; i<list.size(); i++){
            userData.put(list.get(i), data.get(i));
        };
    }

    /**
     * Gets the data of user
     * @return the data of the user
     */
    public LinkedHashMap getUserData() {
        logger.trace("User's data are {}", userData );
        return userData;
    }

    /**
     * Gets the id of user
     * @return the id of the user
     */
    public int getId() {
        logger.trace("User's id is{}", id );
        return id;
    }

    /**
     * Sets user id
     * @param id what user id should be set
     */
    public void setId(int id) {
        logger.trace("Set user id {} to {}", this.id, id);
        this.id = id;
    }
}
