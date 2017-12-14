package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ProductController {
    private static ProductDao productDataStore = ProductDaoMem.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
    private static CartDao cartData = CartDaoMem.getInstance();
    private static OrderDao orderData = OrderDaoMem.getInstance();

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


    public static ModelAndView forms(String title) {
        Map params = new HashMap<>();
        params.put("title", title);
        return new ModelAndView(params, "forms");
    }

    public static ModelAndView forms(String title, String method) {
        Map params = new HashMap<>();
        params.put("title", title);
        params.put("method", method);
        return new ModelAndView(params, "forms");
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
        boolean isError = false;
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
                    isError = true;
                    params.put("errorMessage", "Oh no! You cant order items with different currencies.");
                }
            }


        }
        params.put("isError", isError);
        params.put("sumPrice", sumPrice);
        params.put("currency", firstCurrency);
        return new ModelAndView(params, "product/cart");

    }

    public static void saveData(Request req){

        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        LinkedHashMap userData = new LinkedHashMap();

        for (String data : list){
            userData.put(data, req.queryParams(data));
        }
        Order order = new Order(cartData.getAll(), userData);
        orderData.add(order);

    }

    public static ModelAndView confirmation() {
        int sumPrice = 0;
        Map params = new HashMap();
        for (Map.Entry<Product, Integer> entry : cartData.getAll().entrySet()) {
            sumPrice += entry.getKey().getDefaultPrice();
        }
        cartData.clearCart();
        params.put("message", "Payment successful!");
        params.put("userData", orderData.getLast().getUserData());
        params.put("orderData", orderData.getLast().getOrder());
        params.put("orderId", orderData.getLast().getId());
        params.put("sumPrice", sumPrice);
        return new ModelAndView(params, "confirmation");
    }

    public static void removeProduct(Integer id) {
        cartData.remove(id);
    }

    public static void changeQuantity(Integer id, Integer quantity){
        if (quantity == 0) {
            cartData.remove(id);
        } else {
            cartData.setQuantity(id, quantity);
        }

    }

}
