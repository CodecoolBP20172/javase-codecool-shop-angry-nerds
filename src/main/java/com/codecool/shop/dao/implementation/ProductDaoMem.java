package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoMem implements ProductDao {

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoMem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);


    private ProductDaoMem() {
    }

    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        logger.debug("ProductDaoMem instance created");
        return instance;
    }

    @Override
    public void add(Product product) {
        product.setId(DATA.size()+1);
        DATA.add(product);
        logger.debug("Product added to memory");
    }

    @Override
    public Product find(int id) {
        logger.debug("Product found in memory by id {}", id);
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        logger.debug("Product removed from memory by id {}", id);
        DATA.remove(find(id));
    }

    @Override
    public List<Product> getAll() {
        logger.debug("Getting all products from memory");
        return DATA;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        logger.debug("Getting all products from memory by supplier {}", supplier.getName());
        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        logger.debug("Getting all products from memory by product category {}", productCategory.getName());
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
