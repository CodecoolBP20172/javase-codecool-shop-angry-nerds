package com.codecool.shop.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class Supplier extends BaseModel {
    private ArrayList<Product> products;
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);

    public Supplier(String name, String description) {
        super(name);
        this.products = new ArrayList<>();
        logger.trace("Created instance of class Supplier with name {} and description {}", this.name, this.description);

    }

    public void setProducts(ArrayList<Product> products) {
        logger.trace("Set products supplied by {} to {}", this.name, products.toString());
        this.products = products;
    }

    public ArrayList getProducts() {
        logger.trace("Products supplied by {} are {}", this.name, products.toString());
        return this.products;
    }

    public void addProduct(Product product) {
        logger.trace("{} added to products supplied by {}", product, this.name);
        this.products.add(product);
    }

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