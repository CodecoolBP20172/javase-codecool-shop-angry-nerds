package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a simple product data access object which helps the Product class to save and load
 * data from the memory.
 * <p>
 * It implements the ProductDao interface. It has 6 methods, one, that adds a Product to the memory,
 * one, that removes it, one, that returns all products in a list, one for searching for a product
 * in the memory, and two, that returns all the products by supplier or product category.
 *
 * @author  Márton Ollári
 * @version 1.0
 * @since   2018-01-20
 */
public class ProductDaoMem implements ProductDao {

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, then the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }
    /**
     * This method adds a Product to an ArrayList, which represents the memory. It has
     * one parameter, which is a Product that gets inserted into the list.
     * @param product this is the Product that gets added to the memory
     */
    @Override
    public void add(Product product) {
        product.setId(DATA.size()+1);
        DATA.add(product);
    }
    /**
     * This method searches for the Product in the memory, based on the id of the product.
     * If it does not find the Product, it returns null. However if the Product is
     * found based on the id than it gets returned.
     * @param id the Product will be searched for based on this id
     * @return the Product, which has the corresponding id
     */
    @Override
    public Product find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }
    /**
     * This method finds and removes the Product based on the id given as parameter.
     * @param id the Product will be removed based on this id
     */
    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }
    /**
     * This method gets all the products from the memory and returns them in an ArrayList.
     * @return an ArrayList, which contains all the products
     */
    @Override
    public List<Product> getAll() {
        return DATA;
    }
    /**
     * This method gets all the products from the memory which has the same supplier,
     * as the supplier given as a parameter, and returns them in an ArrayList.
     * @param  supplier the Supplier to get the Products
     * @return an ArrayList, which contains all the products with the same supplier
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }
    /**
     * This method gets all the products from the memory which has the same supplier,
     * as the supplier given as a parameter, and returns them in an ArrayList.
     * @param  productCategory the Supplier to get the Products
     * @return an ArrayList, which contains all the products with the same supplier
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
