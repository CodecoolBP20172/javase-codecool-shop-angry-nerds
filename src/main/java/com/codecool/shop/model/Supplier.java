package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * This class serves as model for every supplier and holds all the information about each of them.
 * It extends the BaseModel abstract class from which it inherits its base fields like id, name and
 * description, to which a class specific field is added, a products list, which contains all the
 * products that belong to this supplier.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class Supplier extends BaseModel {
    private ArrayList<Product> products;
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);

    /**
     * The constructor of the Supplier class
     * @param name the name of the supplier
     * @param description a base description of the supplier
     */
    public Supplier(String name, String description) {
        super(name);
        this.products = new ArrayList<>();
        logger.trace("Created instance of class Supplier with name {} and description {}", this.name, this.description);

    }

    /**
     * Sets which products are supplied by this supplier based on a list got as a parameter
     * @param products an ArrayList with all the products by this supplier
     */
    public void setProducts(ArrayList<Product> products) {
        logger.trace("Set products supplied by {} to {}", this.name, products.toString());
        this.products = products;
    }

    /**
     * Returns all products from this supplier
     * @return an ArrayList with all products by this supplier
     */
    public ArrayList getProducts() {
        logger.trace("Products supplied by {} are {}", this.name, products.toString());
        return this.products;
    }

    /**
     * Adds a product to the supplier product list
     * @param product the product that's added to the products ArrayList
     */
    public void addProduct(Product product) {
        logger.trace("{} added to products supplied by {}", product, this.name);
        this.products.add(product);
    }

    /**
     * Sets a custom toString representation of the class
     * @return the new string format of the Supplier class
     */
    public String toString() {
        logger.trace("The toString format of supplier {} is {}", this.name, this.toString());
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }
}