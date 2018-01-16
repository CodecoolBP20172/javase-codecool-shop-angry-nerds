package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoMem implements ProductCategoryDao {

    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoMem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoMem.class);

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoMem() {
    }

    public static ProductCategoryDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoMem();
        }
        logger.debug("ProductCategoryDaoMem instance created");
        return instance;
    }

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

    @Override
    public ProductCategory find(int id) {
        logger.debug("Product category found in memory and returned");
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.debug("Product category removed from the memory");
    }

    @Override
    public List<ProductCategory> getAll() {
        logger.debug("Returning all product categories stored in memory");
        return DATA;
    }
}
