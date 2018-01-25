package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;
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
    private static ProductDao productDataStore = ProductDaoJDBC.getInstance();

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
    public void add(Product product, int orderId) {
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(String.valueOf(product.getId()), true));
            list.add(new TypeCaster(String.valueOf(orderId), true));
            con.execute("INSERT INTO cart VALUES (DEFAULT, ?, ?);", list);
            logger.debug("Product added to database");
        } catch (SQLException e) {
            logger.warn("Connection to database failed while adding product to database");
            e.printStackTrace();
        }
    }

    @Override
    public Cart find(int orderId) {
        String query = "SELECT product_id FROM cart WHERE order_id = ?;";
        Cart cart = new Cart(orderId);

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(String.valueOf(orderId), true));
            ResultSet rs = conn.process(query, list);
            while (rs.next()) {
                Product product = productDataStore.find(rs.getInt("product_id"));
                cart.add(product);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find product in database");
        }
        if (cart != null) {
            logger.debug("Products found in memory and returned");
            return cart;
        }
        else {
            logger.warn("Product category not found in database");
            return null;
        }
    }

    /**
     * This method is for to count the products in the cart.
     * Iterates through the values of getAll method, incrementing count
     * variable according to the values.
     * @return number of products in the cart.
     */
    @Override
    public int getCount(int orderId) {
        Integer count=0;
        for (Integer value : find(orderId).getCart().values()) {
            count += value;
        }
        logger.debug("Count value of cart: {}", count);
        return count;
    }

    /**
     * This is malfunctioning.
     */
    @Override
    public void removeByOrderId(int orderId) {
        ArrayList<TypeCaster> queryList = new ArrayList<>();
        queryList.add(new TypeCaster(String.valueOf(orderId), true));
        try (ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute("DELETE FROM cart WHERE order_id=?", queryList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeProduct(int productId, int orderId) {
        ArrayList<TypeCaster> queryList = new ArrayList<>();
        queryList.add(new TypeCaster(String.valueOf(productId), true));
        queryList.add(new TypeCaster(String.valueOf(orderId), true));
        try (ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute("DELETE FROM cart WHERE id = any (array(SELECT id FROM cart WHERE product_id = ? AND order_id = ? LIMIT 1));", queryList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void setProductQuantity(int productId, int orderId, int quantity) {
        String query = "SELECT COUNT(*) FROM cart WHERE product_id = ? AND order_id = ?;";
        ArrayList<TypeCaster> queryList = new ArrayList<>();
        queryList.add(new TypeCaster(String.valueOf(productId), true));
        queryList.add(new TypeCaster(String.valueOf(orderId), true));
        try (ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet rs = conn.process(query, queryList);
            rs.next();
            int count = rs.getInt("count");
            int difference = quantity - count;
            if (difference > 0) {
                for (int i = 0; i < difference; i++) {
                    add(productDataStore.find(productId), orderId);
                }
            }
            else if (difference < 0) {
                for (int i = 0; i < -difference; i++) {
                    removeProduct(productId, orderId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
