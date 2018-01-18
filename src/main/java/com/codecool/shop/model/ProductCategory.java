package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProductCategory extends BaseModel {
    private String department;
    private ArrayList<Product> products;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategory.class);

    public ProductCategory(String name, String department, String description) {
        super(name);
        this.department = department;
        this.products = new ArrayList<>();
        logger.trace("Created instance of class ProductCategory with name {}, description {} and department {}", this.name, this.description, this.department);
    }

    public String getDepartment() {
        logger.trace("The department of {} is {}", name, this.department);
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        logger.trace("The department of {} set to {}", name, department);
    }

    public void setProducts(ArrayList<Product> products) {
        logger.trace("Set products in category {} to {}", this.name, products.toString());
        this.products = products;
    }

    public ArrayList getProducts() {
        logger.trace("Products in category {} are {}", this.name, products.toString());
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        logger.trace("{} added to products in category {}", product, this.name);
    }

    public String toString() {
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