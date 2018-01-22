package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple product category data access object which helps the ProductCategory class to save and load
 * data from the memory.
 * <p>
 * It implements the ProductCategoryDao interface. It has 4 methods, one, that adds a ProductCategory to the memory,
 * one, that removes it if necessary, one, that returns all categories in a list and one for searching for a category
 * in the memory.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class ProductCategoryDaoMem implements ProductCategoryDao {

    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoMem.class);

    private ProductCategoryDaoMem() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, then the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        logger.debug("ProductCategoryDaoMem instance created");
        return instance;
    }

    /**
     * This method adds a ProductCategory to an ArrayList, which represents the memory. It has
     * one parameter, which is a ProductCategory that gets inserted into the list. If the argument
     * is null it throws an IllegalArgumentException.
     * @param category this is the ProductCategory that gets added to the memory
     * @throws IllegalArgumentException if the parameter category equals to null
     */
    @Override
    public void add(ProductCategory category) throws IllegalArgumentException {
        if (category == null) {
            logger.debug("ProductCategoryDaoMem add method received invalid argument");
            throw new IllegalArgumentException();
        }
        category.setId(DATA.size() + 1);
        DATA.add(category);
        logger.debug("Product category added successfully to the memory");
    }

    /**
     * This method searches for the ProductCategory in the memory based on the id of the category.
     * If it does not find the ProductCategory, it returns null. However if the ProductCategory is
     * found based on the id than it gets returned.
     * @param id the ProductCategory will be searched for based on this id
     * @return the ProductCategory, which has the corresponding id
     */
    @Override
    public ProductCategory find(int id) {
        logger.debug("Product category found in memory and returned");
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method finds and removes the ProductCategory based on the id given as parameter.
     * @param id the ProductCategory will be removed based on this id
     */
    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.debug("Product category removed from the memory");
    }

    /**
     * This method gets all the product categories from the memory and returns them in an ArrayList.
     * @return an ArrayList, which contains all the product categories
     */
    @Override
    public List<ProductCategory> getAll() {
        logger.debug("Returning all product categories stored in memory");
        return DATA;
    }
}
