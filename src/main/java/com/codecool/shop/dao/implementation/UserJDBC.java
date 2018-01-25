package com.codecool.shop.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    private static UserJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(UserJDBC.class);
    private static int CurrentUserId;

    public static UserJDBC getInstance() {
        if (instance == null) {
            instance = new UserJDBC();
        }
        logger.debug("UserJDBC instance created");
        return instance;
    }

    public static int getCurrentUserId() {
        return CurrentUserId;
    }

    public static void setCurrentUserId(int currentUserId) {
        CurrentUserId = currentUserId;
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
    public User getUser(int id) {
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
                user = new User(id, userData);
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

    public void saveUserData(String name ,String email, String password) {
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

    public boolean emailCheck(String email) {
        try (ConnectionHandler conn = new ConnectionHandler()) {
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(email, false));
            ResultSet resultSet = conn.process("SELECT * from user_data where email=?", args);
            String resultEmail = null;
            while (resultSet.next()){
                resultEmail = resultSet.getString("email");
            }
            return resultEmail == null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPassByEmail(String email) {
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

    public int getUserId(String email) {
        int result = 0;
        try (ConnectionHandler conn = new ConnectionHandler()) {
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(email, false));
            ResultSet resultSet = conn.process("SELECT id FROM user_data WHERE email=?", args);
            if (resultSet == null) {
                throw new IllegalArgumentException();
            }
            while (resultSet.next()) {
                result = resultSet.getInt("id");
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateUser(User user) {
        LinkedHashMap<String, String> userData = user.getUserData();
        String query = "UPDATE user_data SET phone_number = ?, billing_address = ?, billing_city = ?, billing_zipcode = ?, " +
                       "billing_country = ?, shipping_address = ?, shipping_city = ?, shipping_zipcode = ?, shipping_country = ?;";
        ArrayList<TypeCaster> queryList = new ArrayList<>();
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
            logger.debug("User added successfully to database");
            conn.execute(query, queryList);
            }
        catch (SQLException e) {
            logger.warn("Connection to database failed while adding product category to database");
            }
    }

}


