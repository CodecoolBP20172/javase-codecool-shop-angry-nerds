package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple product category data access object which serves the ProductCategory class and helpes him to
 * communicate with the database.
 * <p>
 * It implements the ProductCategoryDao interface. It has 4 methods, one, that adds a ProductCategory to the database,
 * one, that removes it if necessary, one, that returns all categories in a list and one for searching for a category
 * in the database.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-16
 */
public class ProductCategoryDaoJDBC implements ProductCategoryDao{

    private static ProductCategoryDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoJDBC.class);

    private ProductCategoryDaoJDBC() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        logger.debug("ProductCategoryDaoJDBC instance created");
        return instance;
    }

    /**
     * This method adds a ProductCategory to the database. It has one parameter, which is a
     * ProductCategory that gets inserted into the database. If the argument is null it throws
     * an IllegalArgumentException.
     * @param category this is the ProductCategory that gets added to the DB
     * @throws IllegalArgumentException if the parameter category equals to null
     */
    @Override
    public void add(ProductCategory category) throws IllegalArgumentException {
        if (category == null) {
            logger.debug("ProductCategoryDaoJDBC add method received invalid argument");
            throw new IllegalArgumentException();
        }
        String query = "INSERT INTO product_category (name, description, department) VALUES (?,?,?);";
        ArrayList<TypeCaster> queryList = new ArrayList<>();

        queryList.add(new TypeCaster(category.getName(), false));
        queryList.add(new TypeCaster(category.getDescription(), false));
        queryList.add(new TypeCaster(category.getDepartment(), false));
        try(ConnectionHandler conn = new ConnectionHandler()) {
            logger.debug("Product category added successfully to database");
            conn.execute(query, queryList);
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while adding product category to database");
        }
    }

    /**
     * This method searches for the ProductCategory in the database based on the id of the category.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure, if it doesn't find the id in the DB, it returns null.
     * However if the ProductCategory is found based on the id than it gets returned.
     * @param id the ProductCategory will be searched for based on this id
     * @return the ProductCategory, which has the corresponding id
     */
    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_category WHERE id = ?;";
        List<ProductCategory> categoryList = new ArrayList<>();

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(String.valueOf(id), true));
            ResultSet rs = conn.process(query, list);
            while (rs.next()) {
                ProductCategory category = new ProductCategory(rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("description"));
                category.setId(rs.getInt("id"));
                categoryList.add(category);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find product category in database");
        }
        if (categoryList.size() > 0) {
            logger.debug("Product category found in memory and returned");
            return categoryList.get(0);
        }
        else {
            logger.warn("Product category not found in database");
            return null;
        }
    }

    /**
     * This method removes the ProductCategory that has the same id as given by the parameter.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the ProductCategory is found based on
     * the id than it gets removed.
     * @param id the ProductCategory will be removed based on this id
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_category WHERE id = ?;";

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(id), true));
            conn.execute(query, queryList);
            logger.debug("Product category removed from database");
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to remove product category from database");
        }
    }

    /**
     * This method gets all the product categories from the database and returns them in a list.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the connection went through without problems,
     * it then returns all the product categories in an ArrayList.
     * @return an ArrayList, which contains all the product categories
     */
    @Override
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM product_category;";
        ArrayList<ProductCategory> resultList = new ArrayList<>();

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet rs = conn.process(query);
            while (rs.next()) {
                ProductCategory category = new ProductCategory(rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("description"));
                category.setId(rs.getInt("id"));
                resultList.add(category);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying get all product categories from database");
        }
        logger.debug("Returning all product categories in a list");
        return resultList;
    }
}
