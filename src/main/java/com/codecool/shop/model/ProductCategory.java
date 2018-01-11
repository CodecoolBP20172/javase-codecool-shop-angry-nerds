package com.codecool.shop.model;

import java.util.ArrayList;

public class ProductCategory extends BaseModel {
    private String department;
    private ArrayList<Product> products;

    public ProductCategory(String name, String department, String description) {
        super(name);
        this.department = department;
        this.products = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ProductCategory that = (ProductCategory) o;
//
//        return getDepartment() != null ? getDepartment().equals(that.getDepartment()) : that.getDepartment() == null;
//    }
//
//    @Override
//    public int hashCode() {
//        return getDepartment() != null ? getDepartment().hashCode() : 0;
//    }
}