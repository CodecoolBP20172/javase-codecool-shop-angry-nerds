package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CartDaoJDBC implements CartDao {
    private static CartDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    public static CartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        logger.warn("Not working with multiple users");
        logger.debug("Starting method with arg: {}", product);
        if (product == null) {
            logger.warn("Product is null");
            throw new IllegalArgumentException();
        }
        try (ConnectionHandler conn = new ConnectionHandler()) {
            logger.info("Connected to database");
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            logger.debug("resultSet of select * from cart: {}", resultSet);
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            logger.debug("productList from resultSet col 2: {}", productList);
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + product.getId(),false));
            logger.debug("Update cart product_list with: {},{}", productList,product.getId());
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            logger.error("Database connection error");
            e.printStackTrace();
        }
    }


    @Override
    public Map<Product, Integer> getAll() {
        logger.warn("Not working with multiple users");
        Map<Product, Integer> resultMap= new HashMap<>();
        try (ConnectionHandler conn = new ConnectionHandler()) {
            logger.info("Connected to database");
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            logger.debug("resultSet of select * from cart: {}", resultSet);
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            logger.debug("productList from resultSet col 2: {}", productList);
            List<String> productIdList = Arrays.asList(productList.split("\\s*,\\s*"));
            logger.debug("productIdList: {}", productIdList);
            for(String str: productIdList){
                Product product = ProductDaoJDBC.getInstance().find(Integer.parseInt(str));
                if (resultMap.containsKey(product)) {
                    resultMap.put(product, resultMap.get(product)+1);
                } else {
                    resultMap.put(product,1);
                }
            }
            logger.debug("returning cart hashmap: {}", resultMap);
        } catch (SQLException e) {
            logger.error("Database Connection error");
            e.printStackTrace();

        }
        return resultMap;
    }


    @Override
    public int getCount() {
        Integer count=0;
        for (Integer value: getAll().values()) {
            count += value;
        }
        logger.debug("Count value of cart: {}", count);
        return count;
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
        logger.warn("Not working with multiple users");
        try  (ConnectionHandler conn = new ConnectionHandler()) {
            logger.debug("Connected to database");
            ResultSet resultSet= conn.process("SELECT * FROM cart");
            logger.debug("resultSet of select * from cart: {}", resultSet);
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            logger.debug("productList from resultSet col 2: {}", productList);
            List<String> productIdList = new ArrayList<>(Arrays.asList(productList.split("\\s*,\\s*")));
            logger.debug("productIdList: {}", productIdList);
            StringBuilder arg = new StringBuilder();
            for (String productId: productIdList){
                if (!productId.equals(Integer.toString(id))) {
                    arg.append(productId).append(",");
                }
            }
            logger.warn("Problem near; creating arg with stringbuilder: {}", arg.toString());
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(arg.toString().substring(0,arg.length()-1),false));
            logger.debug("Update cart product_list with: {}", arg.toString().substring(0,arg.length()-1));
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            logger.error("Database Connection error");
            e.printStackTrace();
        }
    }

    @Override
    public void setQuantity(int id, int quantity) {
        logger.warn("Not working with multiple users");
        if (quantity < 0) {
            logger.warn("Quantity = {}", quantity);
            throw new IllegalArgumentException();
        }
        if (quantity == 0) {
            logger.debug("Quantity = {}, removing product with id: {}", quantity,id);
            remove(id);
            return;
        }
        try (ConnectionHandler conn = new ConnectionHandler()) {
            logger.debug("Connected to database");
            logger.debug("removing product id: {}", id);
            remove(id);
            ResultSet resultSet = conn.process("SELECT * FROM cart");
            logger.debug("resultSet of select * from cart: {}", resultSet);
            String productList = null;
            while (resultSet.next()) {
                productList = resultSet.getString(2);
            }
            logger.debug("productList from resultSet col 2: {}", productList);
            StringBuilder quantityString = new StringBuilder();
            for (int i = 0; i<quantity; i++) {
                quantityString.append(id).append(",");
            }
            logger.warn("Problem near; creating arg with stringbuilder: {}", quantityString.toString());
            List<TypeCaster> args = new ArrayList<>();
            args.add(new TypeCaster(productList + "," + quantityString.toString().substring(0,quantityString.length()-1),false));
            logger.debug("Update cart product_list with: {},{}", productList, quantityString.toString().substring(0,quantityString.length()-1));
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            logger.error("Database Connection error");
            e.printStackTrace();
        }
    }
}
