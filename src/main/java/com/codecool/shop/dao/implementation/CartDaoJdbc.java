package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDaoJdbc implements CartDao {
    private static CartDaoJdbc instance = null;


    public static CartDaoJdbc getInstance() {
        if (instance == null) {
            instance = new CartDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(Product product) {


    }

    @Override
    public Map<Product, Integer> getAll() {
        try {
            ConnectionHandler conn = new ConnectionHandler();
            ResultSet resultSet= conn.process("SELECT * FROM Cart", new ArrayList<TypeCaster>());
            String productList = null;
            Map<Product, Integer> resultMap= new HashMap<>();
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            String productIdList[] = productList.split(",");
            for(String str: productIdList){
                Product product = ProductDaoJDBC.getInstance().find(Integer.parseInt(str));
                if (resultMap.containsKey(product)) {
                    resultMap.put(product, resultMap.get(product)+1);
                } else {
                    resultMap.put(product,1);
                }
            }
            return resultMap;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void clearCart() {

    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void setQuantity(int id, int quantity) {

    }
}
