package com.codecool.shop.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    private static final Logger logger = LoggerFactory.getLogger(UserJDBC.class);

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
        List<TypeCaster> list = new ArrayList <>();
        list.add(new TypeCaster(String.valueOf(id), true));
        User user = null;
        try(ConnectionHandler handler = new ConnectionHandler()) {
            ResultSet rs = handler.process(query, list);
            while (rs.next()){
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String billingAddress = rs.getString("billing_address");
                String billingCity = rs.getString("billing_city");
                String billingZipcode = rs.getString("billing_zipcode");
                String billingCountry = rs.getString("billing_country");
                String shippingAddress = rs.getString("shipping_address");
                String shippingCity = rs.getString("shipping_city");
                String shippingZipcode = rs.getString("shipping_zipcode");
                String shippingCountry = rs.getString("shipping_country");
                user = new User(name, email, phoneNumber, billingAddress, billingCity, billingZipcode, billingCountry,
                        shippingAddress, shippingCity, shippingZipcode, shippingCountry);
            }
        } catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find user in database");
            e.printStackTrace();
        }
        if (user == null){
            logger.warn("User not found in database");
            return user;
        }
        logger.debug("User found in memory and returned");
        return user;
    }

    public static void saveUserData(String name ,String email, String password) {
        if (emailCheck(email)) {
            try (ConnectionHandler conn = new ConnectionHandler()) {
                List<TypeCaster> args = new ArrayList<>();
                args.add(new TypeCaster(name, false));
                args.add(new TypeCaster(email, false));
                args.add(new TypeCaster(password, false));
                conn.execute("INSERT INTO user_data (id,name,email,password) VALUES(DEFAULT, ?, ?, ?)", args);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean emailCheck(String email) {
        try (ConnectionHandler conn = new ConnectionHandler()) {
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(email, false));
            ResultSet resultSet = conn.process("SELECT * from user_data where email=?", args);
            return resultSet != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPassByEmail(String email) {
        try (ConnectionHandler conn = new ConnectionHandler()) {
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(email, false));
            ResultSet resultSet = conn.process("SELECT password FROM user_data WHERE email=?", args);
            if (resultSet == null) {
                throw new IllegalArgumentException();
            }
            String result = "";
            while (resultSet.next()) {
                result =  resultSet.getString("password");
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


