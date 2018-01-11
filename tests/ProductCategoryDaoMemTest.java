package com.codecool.shop.dao;

import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductCategoryDaoMemTest extends ProductCategoryDaoTest<ProductCategoryDaoMem> {

    @Override
    protected ProductCategoryDaoMem createInstance() {
        return ProductCategoryDaoMem.getInstance();
    }

}