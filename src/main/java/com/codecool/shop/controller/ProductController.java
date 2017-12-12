package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class ProductController {
    private static ProductDao productDataStore = ProductDaoMem.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

    public static ModelAndView renderProducts(Request req, Response res) {

        Map params = new HashMap<>();

        params.put("category", productCategoryDataStore.find(1));
        params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderProductsBy(String supOrCat, String id) {
        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.find(1));
        System.out.println(supOrCat + id);
        if (supOrCat.equals("supplier")){
            params.put("products", productDataStore.getBy(supplierDataStore.find(Integer.parseInt(id))));
        } else if ( supOrCat.equals("category")) {
            System.out.println(111);
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(Integer.parseInt(id))));
        } else {
            System.out.println(122);
            return new ModelAndView(params, "404");
        }
        return new ModelAndView(params, "product/index");
    }

}
