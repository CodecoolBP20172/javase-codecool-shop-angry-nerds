package com.codecool.shop.dao.implementation;

import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserJDBC {

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
            e.printStackTrace();
        }
        return user;
    }
}


