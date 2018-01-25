package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
     * The constructor of the User class
     * @param userData gets a Map as parameter with all the user data
     */
    public User(LinkedHashMap<String, String> userData){

        this.userData = userData;

    }

    public User(int id, LinkedHashMap<String, String> userData){
        this.id = id;
        this.userData = userData;

    }

    /**
     * Gets the data of user
     * @return the data of the user
     */
    public LinkedHashMap<String, String> getUserData() {
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
