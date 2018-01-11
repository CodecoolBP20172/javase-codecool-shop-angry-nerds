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
        try (ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            List<TypeCaster> arg = new ArrayList<>();
            arg.add(new TypeCaster(product.getName(),false));
            resultSet = conn.process("SELECT id FROM product WHERE name =?", arg);
            String thisIsAMess = null;
            while (resultSet.next()) {
                thisIsAMess = resultSet.getString("id");
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + thisIsAMess,false));
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<Product, Integer> getAll() {
        Map<Product, Integer> resultMap= new HashMap<>();
        try (ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            String productList = null;
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

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return resultMap;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void clearCart() {
        try (ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute("DELETE FROM cart");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void remove(int id) {
        try  (ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            List<String> productIdList = new ArrayList<>(Arrays.asList(productList.split("\\s*,\\s*")));
            StringBuilder arg = new StringBuilder();
            for (String productId: productIdList){
                if (!productId.equals(Integer.toString(id))) {
                    arg.append(productId).append(",");
                }
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(arg.toString().substring(0,arg.length()-1),false));
            System.out.println(arg.toString().substring(0,arg.length()-1));
            conn.execute("UPDATE cart SET product_list = ?",args);
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
            return;
        }
        try (ConnectionHandler conn = new ConnectionHandler()) {
            remove(id);
            ResultSet resultSet = conn.process("SELECT * FROM cart");
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            StringBuilder quantityString = new StringBuilder();
            for (int i = 0; i<quantity; i++) {
                quantityString.append(id).append(",");
            }
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + quantityString.toString().substring(0,quantityString.length()-1),false));
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
