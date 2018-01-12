package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class User{

    private LinkedHashMap userData;
    private int id;

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

    public LinkedHashMap getUserData() {
        return userData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
