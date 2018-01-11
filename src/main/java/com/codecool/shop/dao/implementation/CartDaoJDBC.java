package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CartDaoJDBC implements CartDao {
    private static CartDaoJDBC instance = null;


    public static CartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        if (product == null) {
            throw new IllegalArgumentException();
        }
        try {
            ConnectionHandler conn = new ConnectionHandler();
            ResultSet resultSet= conn.process("SELECT * FROM Cart");
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + product.getId(),false));
            conn.execute("UPDATE Cart SET product_list = ?",args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<Product, Integer> getAll() {
        try {
            ConnectionHandler conn = new ConnectionHandler();
            ResultSet resultSet= conn.process("SELECT * FROM Cart");
            String productList = null;
            Map<Product, Integer> resultMap= new HashMap<>();
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            List<String> productIdList = Arrays.asList(productList.split("\\s*,\\s*"));
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
        try {
            ConnectionHandler conn = new ConnectionHandler();
            ResultSet resultSet= conn.process("SELECT * FROM Cart");
            String productList = null;
            Map<Product, Integer> resultMap= new HashMap<>();
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            List<String> productIdList = Arrays.asList(productList.split("\\s*,\\s*"));
            productIdList.removeAll(Collections.singleton(Integer.toString(id)));
            String arg = null;
            for (String productId: productIdList){
                arg += productId;
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(arg,false));
            conn.execute("UPDATE Cart SET product_list = ?",args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setQuantity(int id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        if (quantity == 0) {
            remove(id);
        }
        try {
            ConnectionHandler conn = new ConnectionHandler();
            remove(id);
            ResultSet resultSet = conn.process("SELECT * FROM Cart");
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            String productIdList[] = productList.split(",");
            String quantityString = null;
            for (int i = 0; i<quantity; i++) {
                quantityString += "," + id;
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + quantityString,false));
            conn.execute("UPDATE Cart SET product_list = ?",args);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
