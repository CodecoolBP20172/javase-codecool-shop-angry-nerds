package com.codecool.shop.controller;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ProductController {
    private static ProductDao productDataStore = ProductDaoMem.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
    private static CartDao cartData = CartDaoMem.getInstance();

    public static ModelAndView renderProducts(Request req, Response res) {

        Map params = new HashMap<>();
        params.put("title", "All products");
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("products", productDataStore.getAll());
        params.put("cartSize", cartData.getCount());
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderProductsBy(String supOrCat, String id) {
        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("cartSize", cartData.getCount());
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
        System.out.println(params);
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView addProduct(String id) {
        Map params = new HashMap<>();
        params.put("title", "All products");
        params.put("products", productDataStore.getAll());
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        Product product = productDataStore.find(Integer.parseInt(id));
        cartData.add(product);
        params.put("cartSize", cartData.getCount());

        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderCart() {
        int sumPrice = 0;
        String firstCurrency = null;
        String nextCurrency = null;
        boolean differCurrency = false;
        Map params = new HashMap<>();
        params.put("title", "Your cart");
        params.put("cartProducts", cartData.getAll());
        params.put("cartSize", cartData.getCount());
        int firstLoop = 0;
        for (Map.Entry<Product, Integer> entry : cartData.getAll().entrySet()) {
            sumPrice += entry.getKey().getDefaultPrice()*entry.getValue();
            if (firstLoop++ == 0) {
                firstCurrency = entry.getKey().getDefaultCurrency().toString();
            } else {
                nextCurrency = entry.getKey().getDefaultCurrency().toString();
                if (!firstCurrency.equals(nextCurrency)) {
                    differCurrency = true;
                }
            }
        }
        if (differCurrency){
            sumPrice = 0;
            firstCurrency = ", You cant order items with different currencies!";

        }
        params.put("sumPrice", sumPrice);
        params.put("currency", firstCurrency);
        return new ModelAndView(params, "product/cart");

    }

}
