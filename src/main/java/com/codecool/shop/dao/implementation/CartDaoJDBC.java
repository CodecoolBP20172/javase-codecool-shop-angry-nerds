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

/**
 *This java class is updating, deleting and adding data to the database.
 *<h2>This needs a serious refactor!</h2>
 *Called from ProductController.
 *
 * <p>
 *It implements the cartDao interface.
 *Calls ConnectionHandler to execute queries.
 *
 * @author  Matthew Summer
 * @version 4.20
 * @since   2018-01-21
 */

public class CartDaoJDBC implements CartDao {
    private static CartDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * Checks if a CartDaoJDBC instance exists. If so returns it. Otherwise it creates a new.
     * @return instance
     */
    public static CartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new CartDaoJDBC();
        }
        return instance;
    }

    /**
     * It is to add a product to the cart table.
     * If product is null it throws an exception.
     * Sets up the connection with ConnectionHandler in a try/catch block.
     * The connection handled as a resource to auto close it.
     * It selects all from cart, then picks the second column(product id list),
     * then updates the column with the old values and the desired products ID.
     * Throws exception if the database connection is failing.
     *
     * @param product the desired product to add.
     */
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

    /**
     * This method is for to get all the products from cart.
     * Selects all from cart, takes the second column(products id list),
     * then transforms it to a hashmap.
     * @return hashmap with products in the cart.
     */
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

    /**
     * This method is for to count the products in the cart.
     * Iterates through the values of getAll method, incrementing count
     * variable according to the values.
     * @return number of products in the cart.
     */
    @Override
    public int getCount() {
        Integer count=0;
        for (Integer value: getAll().values()) {
            count += value;
        }
        logger.debug("Count value of cart: {}", count);
        return count;
    }

    /**
     * This is malfunctioning.
     */
    @Override
    public void clearCart() {
        try (ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute("DELETE FROM cart CASCADE");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is to remove a product from cart table.
     * Selects all from cart, takes the second column(product list),
     * then iterates through it, and if the id in product list does not match
     * with the param id, appends a string with it + ",". (This solution is malfunctioning
     * in case there is only 1 product left!!)
     * Then cuts the built string last char and updates the cart with it.
     *
     * @param id desired product id to remove
     */
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
            if (args.size() == 0){
                clearCart();
                return;
            }
            args.add(new TypeCaster(arg.toString().substring(0,arg.length()-1),false));
            logger.debug("Update cart product_list with: {}", arg.toString().substring(0,arg.length()-1));
            conn.execute("UPDATE cart SET product_list = ?",args);
        } catch (SQLException e) {
            logger.error("Database Connection error");
            e.printStackTrace();
        }
    }

    /**
     * This method is for to set quantity of a target product in the cart.
     * If quantity is less than zero, throws exception.
     * In case quantity equals zero, it calls the remove method on the target product
     * and returns.
     * Otherwise it calls remove on the target id then,
     * selects all from cart, takes the second column(product list),
     * then appends the product list + "," with the id of the target product as many times
     * as the quantity was set.(This solution is malfunctioning
     * in case there is only 1 product left!!)
     * Updates cart with the new product list.
     * @param id product id
     * @param quantity desired quantity
     */
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
