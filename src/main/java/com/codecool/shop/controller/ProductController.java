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
        params.put("title", "All products");
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("products", productDataStore.getAll());
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderProductsBy(String supOrCat, String id) {
        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        System.out.println(supOrCat + id);
        if (supOrCat.equals("supplier")){
            Supplier supplier = supplierDataStore.find(Integer.parseInt(id));
            params.put("title", supOrCat + " : " +  supplier.getName());
            params.put("products", productDataStore.getBy(supplier));
        } else if ( supOrCat.equals("category")) {
            ProductCategory productCategory = productCategoryDataStore.find(Integer.parseInt(id));
            params.put("title", supOrCat + " : " +  productCategory.getName());
            params.put("products", productDataStore.getBy(productCategory));
        } else {
            return new ModelAndView(params, "404");
        }
        return new ModelAndView(params, "product/index");
    }

}
