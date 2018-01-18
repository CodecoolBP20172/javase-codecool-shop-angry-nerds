package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * This class serves as model for every product category and holds all the information about each of them.
 * It extends the BaseModel abstract class from which it inherits its base fields like id, name and description,
 * to which two more class specific fields are added, the department and a products list, which contains all the
 * products that belong to this category.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class ProductCategory extends BaseModel {
    private String department;
    private ArrayList<Product> products;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategory.class);

    /**
     * The constructor of the ProductCategory class
     * @param name the name of the product category
     * @param department the department of the product category
     * @param description a base description of the category
     */
    public ProductCategory(String name, String department, String description) {
        super(name);
        this.department = department;
        this.products = new ArrayList<>();
        logger.trace("Created instance of class ProductCategory with name {}, description {} and department {}", this.name, this.description, this.department);
    }

    /**
     * Gets the department of the category
     * @return the department of the category
     */
    public String getDepartment() {
        logger.trace("The department of {} is {}", name, this.department);
        return department;
    }

    /**
     * Sets the department of the product category based on its parameter
     * @param department sets the department of the category to this value
     */
    public void setDepartment(String department) {
        this.department = department;
        logger.trace("The department of {} set to {}", name, department);
    }

    /**
     * Sets which products are included in this category based on a list got as a parameter
     * @param products an ArrayList with all the products that should be in this category
     */
    public void setProducts(ArrayList<Product> products) {
        logger.trace("Set products in category {} to {}", this.name, products.toString());
        this.products = products;
    }

    /**
     * Returns all products in this category
     * @return an ArrayList with all products in this category
     */
    public ArrayList getProducts() {
        logger.trace("Products in category {} are {}", this.name, products.toString());
        return this.products;
    }

    /**
     * Adds a product to the product list in this category
     * @param product the product that's added to the category
     */
    public void addProduct(Product product) {
        this.products.add(product);
        logger.trace("{} added to products in category {}", product, this.name);
    }

    /**
     * Sets a custom toString representation of the class
     * @return the new string format of the ProductCategory class
     */
    public String toString() {
        logger.trace("The toString format of product category {} is {}", this.name, this.toString());
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }

}