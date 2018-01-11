package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {

    void add(ProductCategory category) throws IllegalArgumentException;
    ProductCategory find(int id);
    void remove(int id);

    List<ProductCategory> getAll();

}
