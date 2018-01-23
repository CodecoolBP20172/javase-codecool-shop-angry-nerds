package com.codecool.shop.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * This is a simple user data access object which serves the ProductCategory class and helpes him to
 * communicate with the database.
 * <p>
 * It should implement the UserDao interface which does not exist. So ...
 * It has 1 method which is for searching for a user in the database.
 *
 * @author  Zsuzsa Petho
 * @version 1.0
 * @since   2018-01-20
 */
public class UserJDBC {

    private static UserJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(UserJDBC.class);
    private int currentUser;

    private UserJDBC() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static UserJDBC getInstance() {
        if (instance == null) {
            instance = new UserJDBC();
        }
        logger.debug("UserJDBC instance created");
        return instance;
    }

    public int getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * This method searches for the user in the database based on the id of the user.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure, if it doesn't find the id in the DB, it returns null.
     * However if the User is found based on the id than it gets returned.
     * @param id the User will be searched for based on this id
     * @return the User, which has the corresponding id
     */
    public static User getUser(int id) {
        String query = "SELECT * FROM user_data WHERE id = ?";
        LinkedHashMap<String, String> userData = new LinkedHashMap<String, String>();
        List<TypeCaster> list = new ArrayList <>();
        list.add(new TypeCaster(String.valueOf(id), true));
        User user = null;
        try(ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query, list);
            while (rs.next()){
                userData.put("Name", rs.getString("name"));
                userData.put("E-mail", rs.getString("email"));
                userData.put("Phone Number", rs.getString("phone_number"));
                userData.put("Billing Address", rs.getString("billing_address"));
                userData.put("Billing City", rs.getString("billing_city"));
                userData.put("Billing Zipcode", rs.getString("billing_zipcode"));
                userData.put("Billing Country", rs.getString("billing_country"));
                userData.put("Shipping Address", rs.getString("shipping_address"));
                userData.put("Shipping City", rs.getString("shipping_city"));
                userData.put("Shipping Zipcode", rs.getString("shipping_zipcode"));
                userData.put("Shipping Country", rs.getString("shipping_country"));
                user = new User(userData);
            }
        } catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find user in database");
        }
        if (user == null){
            logger.warn("User not found in database");
        }
        logger.debug("User found in memory and returned");
        return user;
    }

    public void saveUser(User user) {
        LinkedHashMap<String, String> userData = user.getUserData();
        String query = "INSERT INTO user_data VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        ArrayList<TypeCaster> queryList = new ArrayList<>();
        queryList.add(new TypeCaster(userData.get("Name"), false));
        queryList.add(new TypeCaster(userData.get("E-mail"), false));
        queryList.add(new TypeCaster(userData.get("Phone Number"), false));
        queryList.add(new TypeCaster(userData.get("Billing Address"), false));
        queryList.add(new TypeCaster(userData.get("Billing City"), false));
        queryList.add(new TypeCaster(userData.get("Billing Zipcode"), false));
        queryList.add(new TypeCaster(userData.get("Billing Country"), false));
        queryList.add(new TypeCaster(userData.get("Shipping Address"), false));
        queryList.add(new TypeCaster(userData.get("Shipping City"), false));
        queryList.add(new TypeCaster(userData.get("Shipping Zipcode"), false));
        queryList.add(new TypeCaster(userData.get("Shipping Country"), false));
        try(ConnectionHandler conn = new ConnectionHandler()) {
            logger.debug("Product category added successfully to database");
            conn.execute(query, queryList);
            setCurrentUser(user.getId());

        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while adding product category to database");
        }
    }

}


