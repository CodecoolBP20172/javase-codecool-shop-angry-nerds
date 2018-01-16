package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductController {
    private static ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private static CartDao cartData = CartDaoJDBC.getInstance();
    private static OrderDao orderData = OrderDaoJDBC.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public static ModelAndView renderProducts(Request req, Response res) {
        logger.info("Rendering index page with all the products...");
        Map params = new HashMap<>();
        params.put("title", "All products");
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("products", productDataStore.getAll());
        params.put("cartSize", cartData.getCount());
        logger.debug("Rendering index page with params:{}",params);
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderProductsBy(String supOrCat, String id) {
        logger.info("Sorting index page by: {}", supOrCat);
        logger.debug("Args: supOrCat: {}, id: {}",supOrCat,id);
        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("cartSize", cartData.getCount());
        if (supOrCat.equals("supplier")){
            Supplier supplier = supplierDataStore.find(Integer.parseInt(id));
            logger.warn("Find supplier by id {} = {}", id, supplier);
            params.put("title", supOrCat + " : " +  supplier.getName());
            params.put("products", productDataStore.getBy(supplier));
        } else if ( supOrCat.equals("category")) {
            ProductCategory productCategory = productCategoryDataStore.find(Integer.parseInt(id));
            logger.warn("Find category by id {} = {}", id, productCategory);
            params.put("title", supOrCat + " : " +  productCategory.getName());
            params.put("products", productDataStore.getBy(productCategory));
        } else {
            logger.error("SupOrCat is invalid: {} ", supOrCat);
            return new ModelAndView(params, "404");
        }
        logger.debug("Rendering index page with params: {} ", params);
        return new ModelAndView(params, "product/index");
    }


    public static ModelAndView forms(String title) {
        logger.info("Rendering forms page with title: {}", title);
        Map params = new HashMap<>();
        params.put("title", title);
        logger.debug("Rendering forms page with params: {}", params);
        return new ModelAndView(params, "forms");
    }

    public static ModelAndView forms(String title, String method) {
        logger.info("Rendering forms page with title: {}", title);
        Map params = new HashMap<>();
        params.put("title", title);
        params.put("method", method);
        logger.debug("Rendering forms page with params: {}", params);
        return new ModelAndView(params, "forms");
    }

    public static ModelAndView addProduct(String id) {
        logger.info("Adding product to cart...");
        Map params = new HashMap<>();
        params.put("title", "All products");
        params.put("products", productDataStore.getAll());
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        Product product = productDataStore.find(Integer.parseInt(id));
        logger.warn("Find product by id {} = {}", id, product);
        cartData.add(product);
        params.put("cartSize", cartData.getCount());
        logger.debug("Rendering index page with params: {}", params);
        return new ModelAndView(params, "product/index");
    }

    public static ModelAndView renderCart() {
        logger.info("Rendering cart...");
        int sumPrice = 0;
        String firstCurrency = null;
        String nextCurrency = null;
        boolean isError = false;
        Map params = new HashMap<>();
        params.put("title", "Your cart");
        params.put("cartProducts", cartData.getAll());
        params.put("cartSize", cartData.getCount());
        int firstLoop = 0;
        logger.info("Checking if currencies are the same in cart ...");
        for (Map.Entry<Product, Integer> entry : cartData.getAll().entrySet()) {
            sumPrice += entry.getKey().getDefaultPrice()*entry.getValue();
            if (firstLoop++ == 0) {
                firstCurrency = entry.getKey().getDefaultCurrency().toString();
            } else {
                nextCurrency = entry.getKey().getDefaultCurrency().toString();
                if (!firstCurrency.equals(nextCurrency)) {
                    isError = true;
                    logger.error("Found 2 different currencies in cart: {} != {}", firstCurrency, nextCurrency);
                    params.put("errorMessage", "Oh no! You cant order items with different currencies.");
                }
            }
        }
        if (isError == false) {
            logger.info("Currency check passed, no anomaly detected.");
        }
        params.put("isError", isError);
        params.put("sumPrice", sumPrice);
        params.put("currency", firstCurrency);
        logger.debug("Rendering cart with params: {}", params);
        return new ModelAndView(params, "product/cart");

    }

    public static void saveData(Request req){
        logger.info("Saving order to file...");
        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        LinkedHashMap userData = new LinkedHashMap();
        JSONObject json = new JSONObject();

        for (String data : list){
            userData.put(data, req.queryParams(data));
            json.put(data, req.queryParams(data));
        }
        Order order = new Order(cartData.getAll(), userData);
        logger.debug("New order: {}", order);
        orderData.add(order);
        json.put("Ordered Items", cartData.getAll());
        json.put("Order ID", orderData.getLast().getId());
        logger.info("Writing order to file...");
        try (FileWriter file = new FileWriter("target/json/order" + orderData.getLast().getId() + ".json")) {

            file.write(json.toJSONString());
            file.flush();
            logger.info("Order's successfully written to a file.");

        } catch (IOException e) {
            logger.error("Couldn't write order to a file.");
            e.printStackTrace();
        }
    }

    public static ModelAndView confirmation() {
        int sumPrice = 0;
        Map params = new HashMap();
        for (Map.Entry<Product, Integer> entry : cartData.getAll().entrySet()) {
            sumPrice += entry.getKey().getDefaultPrice() * entry.getValue();
        }
        logger.error("CLEAR CART IS NOT WORKING ATM");
        cartData.clearCart();
        params.put("message", "Payment successful!");
        params.put("userData", orderData.getLast().getUserData());
        params.put("orderData", orderData.getLast().getOrder());
        params.put("orderId", orderData.getLast().getId());
        params.put("sumPrice", sumPrice);
        logger.info("Sending email with order...");
        logger.debug("Sending email with order: {}", orderData.getLast());
        Email.sendEmail(orderData.getLast());
        return new ModelAndView(params, "confirmation");
    }

    public static void removeProduct(Integer id) {
        logger.info("Removing product...");
        logger.debug("Removing product with id: {}", id);
        cartData.remove(id);
    }

    public static void changeQuantity(Integer id, Integer quantity){
        logger.info("Changing quantity of a product..");
        if (quantity == 0) {
            logger.debug("Quantity = {}. Removing product with id: {}",quantity, id);
            cartData.remove(id);
        } else {
            logger.debug("Quantity = {}. Setting quantity of the product with id: {}",quantity, id);
            cartData.setQuantity(id, quantity);
        }

    }

}
