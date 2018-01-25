package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;

import spark.Request;
import spark.Response;
import spark.ModelAndView;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *This java class is the bridge between the server and the JDBC classes.
 * <p>
 *It's called from the server and corresponding to the request it takes and returns
 *the information as params alongside with a html shell.
 *
 * @author  Matthew Summer
 * @version 4.20
 * @since   2018-01-21
 */

public class ProductController {
    private static ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private static ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private static SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private static CartDao cartData = CartDaoJDBC.getInstance();
    private static OrderDao orderData = OrderDaoJDBC.getInstance();
    private static UserJDBC userDataJDBC = UserJDBC.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);


    private static int getUserIdFromSession(Request req) {
        return Integer.valueOf(req.session().attribute("userId"));
    }

    private static int getOrderIdFromSession(Request req) {
        return orderData.findByUserIdAndStatus(Integer.valueOf(req.session().attribute("userId")), Status.IN_CART).getId();
    }

    /**
     * Called with the root "/".
     * @param req unused
     * @param res unused
     * @return Returns the title of the page, the categories,
     * suppliers,products, cart size and the index html form.
     */
    public static ModelAndView renderProducts(Request req, Response res) {
        logger.info("Rendering index page with all the products...");
        Map params = new HashMap<>();
        params.put("session" ,req.session().attribute("email"));
        params.put("title", "All products");
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("products", productDataStore.getAll());
        if (req.session().attribute("userId") != null) {
            System.out.println(req.session().attribute("userId").toString());
        }
        if (req.session().attribute("userId") == null) {
            params.put("cartSize", 0);
        }
        else {
            params.put("cartSize", cartData.getCount(getUserIdFromSession(req)));
        }
        logger.debug("Rendering index page with params:{}",params);
        return new ModelAndView(params, "product/index");
    }

    /**
     * This method is to filter products at the index page by a supplier or category.
     * @param supOrCat supplier or category.
     * @param id supplier's or the category's id.
     * @return Returns the title of the page, the categories,
     * suppliers,products filtered by the params, cart size and the index html form.
     */

    public static ModelAndView renderProductsBy(Request req ,String supOrCat, String id) {
        logger.info("Sorting index page by: {}", supOrCat);
        logger.debug("Args: supOrCat: {}, id: {}",supOrCat,id);
        Map params = new HashMap<>();
        params.put("session" ,req.session().attribute("email"));
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        params.put("cartSize", cartData.getCount(getUserIdFromSession(req)));
        if (supOrCat.equals("supplier")){
            Supplier supplier = supplierDataStore.find(Integer.parseInt(id));
            logger.debug("Find supplier by id {} = {}", id, supplier);
            params.put("title", supOrCat + " : " +  supplier.getName());
            params.put("products", productDataStore.getBy(supplier));
        } else if ( supOrCat.equals("category")) {
            ProductCategory productCategory = productCategoryDataStore.find(Integer.parseInt(id));
            logger.debug("Find category by id {} = {}", id, productCategory);
            params.put("title", supOrCat + " : " +  productCategory.getName());
            params.put("products", productDataStore.getBy(productCategory));
        } else {
            logger.error("SupOrCat is invalid: {} ", supOrCat);
            params.put("error", "404");
            params.put("message", "Page not found");
            return new ModelAndView(params, "error");
        }
        logger.debug("Rendering index page with params: {} ", params);
        return new ModelAndView(params, "product/index");
    }

    /**
     * This lovely method is for the forms page.
     * @param title title of the page
     * @return returns the title param and forms html form.
     */

    public static ModelAndView forms(String title, Request req) {
        logger.info("Rendering forms page with title: {}", title);
        Map params = new HashMap<>();
        params.put("session" ,req.session().attribute("email"));
        params.put("title", title);
        logger.debug("Rendering forms page with params: {}", params);
        return new ModelAndView(params, "forms");
    }

    /**
     * This one is an overloaded forms.
     * @param title title of the page
     * @param method holds information about desired payment method(credit card / paypal)
     * @return title,method and forms html form
     */
    public static ModelAndView forms(String title, String method, Request req) {
        logger.info("Rendering forms page with title: {}", title);
        Map params = new HashMap<>();
        params.put("session" ,req.session().attribute("email"));
        params.put("title", title);
        params.put("method", method);
        logger.debug("Rendering forms page with params: {}", params);
        return new ModelAndView(params, "forms");
    }

    /**
     * This one is to add products to the cart.
     * @param id the product id which the user wished to add to cart.
     * @return returns the index page! This one could seriously  use a refactor.
     */
    public static ModelAndView addProduct(Request req, String id) {
        logger.info("Adding product to cart...");
        Map params = new HashMap<>();
        params.put("title", "All products");
        params.put("products", productDataStore.getAll());
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        Product product = productDataStore.find(Integer.parseInt(id));
        logger.debug("Find product by id {} = {}", id, product);
        cartData.add(product, getOrderIdFromSession(req));
        params.put("cartSize", cartData.getCount(getUserIdFromSession(req)));
        logger.debug("Rendering index page with params: {}", params);
        return new ModelAndView(params, "product/index");
    }

    /**
     * This method is to show the cart. It also sums up all items price in the cart and checks
     * if the currency equals to one another.
     * @return Returns the title, products in the cart, cart size, isError(if an error happened,
     * this value will show), sum of the price, currency and the cart html form.
     */

    public static ModelAndView renderCart(Request req) {
        logger.info("Rendering cart...");
        int sumPrice = 0;
        String firstCurrency = null;
        String nextCurrency = null;
        boolean isError = false;
        Map params = new HashMap<>();
        params.put("session" ,req.session().attribute("email"));
        params.put("title", "Your cart");
        params.put("cartProducts", cartData.find(getOrderIdFromSession(req)).getCart());
        params.put("cartSize", cartData.getCount(getOrderIdFromSession(req)));
        int firstLoop = 0;
        logger.info("Checking if currencies are the same in cart ...");
        for (Map.Entry<Product, Integer> entry : cartData.find(getOrderIdFromSession(req)).getCart().entrySet()) {
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
        if (!isError) {
            logger.info("Currency check passed, no anomaly detected.");
        }
        params.put("isError", isError);
        params.put("sumPrice", sumPrice);
        params.put("currency", firstCurrency);
        logger.debug("Rendering cart with params: {}", params);
        return new ModelAndView(params, "product/cart");

    }

    /**
     * Saving order data to a file.
     * @param req the order params
     */

    public static void saveData(Request req){
        logger.info("Saving order to file...");
        List<String> list = new ArrayList<>(Arrays.asList("Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country", "Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        LinkedHashMap userData = new LinkedHashMap();
        JSONObject json = new JSONObject();

        for (String data : list){
            userData.put(data, req.queryParams(data));
            json.put(data, req.queryParams(data));
        }
        User user = new User(userData);
        userDataJDBC.updateUser(user);
        Order order = new Order(getOrderIdFromSession(req), userDataJDBC.getUser(getUserIdFromSession(req)), cartData.find(getOrderIdFromSession(req)), Status.CHECKED_OUT);
        logger.debug("New order: {}", order);
        orderData.add(order);
        json.put("Ordered Items", cartData.find(getOrderIdFromSession(req)));
        json.put("Order ID", getOrderIdFromSession(req));
        logger.info("Writing order to file...");
        try (FileWriter file = new FileWriter("target/json/order" + getOrderIdFromSession(req) + ".json")) {

            file.write(json.toJSONString());
            file.flush();
            logger.info("Order's successfully written to a file.");

        } catch (IOException e) {
            logger.error("Couldn't write order to a file.");
            e.printStackTrace();
        }
    }


    /**
     * This method is for teh confirmation page. Also sends an email about it.
     * @return Returns order, user data, the order ID, sum of the price,
     * a message if the process was successful and the confirmation html form.
     */
    public static ModelAndView confirmation(Request req) {
        int sumPrice = 0;
        Map params = new HashMap();
        for (Map.Entry<Product, Integer> entry : cartData.find(getOrderIdFromSession(req)).getCart().entrySet()) {
            sumPrice += entry.getKey().getDefaultPrice() * entry.getValue();
        }
        params.put("session" ,req.session().attribute("email"));
        params.put("message", "Payment successful!");
        params.put("userData", userDataJDBC.getUser(getUserIdFromSession(req)).getUserData());
        params.put("orderData", orderData.findByUserIdAndStatus(getUserIdFromSession(req),Status.CHECKED_OUT).getCart());
        params.put("orderId", getOrderIdFromSession(req));
        params.put("sumPrice", sumPrice);
        logger.info("Sending email with order...");
        Email.sendEmail(orderData.findByUserIdAndStatus(getUserIdFromSession(req),Status.CHECKED_OUT), userDataJDBC.getUser(getUserIdFromSession(req)));
        Order order = new Order(userDataJDBC.getUser(getUserIdFromSession(req)));
        return new ModelAndView(params, "confirmation");
    }

    /**
     * Calls the removeByOrderId method for the target product from the cart.
     * @param id product id which to removeByOrderId.
     */
    public static void removeProduct(Integer id) {
        logger.info("Removing product...");
        logger.debug("Removing product with id: {}", id);
        cartData.removeByOrderId(id);
    }

    /**
     * Changes a quantity of the target product in the cart.
     * If changed to 0, calls removeByOrderId, otherwise setProductQuantity.
     * @param id product id which to set quantity on.
     * @param quantity numeric value of quantity to set on.
     */
    public static void changeQuantity(Request req, Integer id, Integer quantity){
        logger.info("Changing quantity of a product..");
        if (quantity == 0) {
            logger.debug("Quantity = {}. Removing product with id: {}",quantity, id);
            cartData.removeByOrderId(id);
        } else {
            logger.debug("Quantity = {}. Setting quantity of the product with id: {}",quantity, id);
            cartData.setProductQuantity(id, getOrderIdFromSession(req), quantity);
        }

    }

    public static ModelAndView login(Request req, Response res){
        Map params = new HashMap();
        params.put("title", "Login");
        return new ModelAndView(params, "login");
    }

    public static ModelAndView signUp(Request req, Response res){
        Map params = new HashMap();
        params.put("title", "Sign Up");
        return new ModelAndView(params, "signUp");
    }

    public static void saveUser(Request req, Response res) {
        String password = com.codecool.shop.Password.hashPassword(req.queryParams("password"));
        userDataJDBC.saveUserData(req.queryParams("name"), req.queryParams("email"), password);
        LinkedHashMap<String, String> userData = new LinkedHashMap<>();
        userData.put("Name", req.queryParams("name"));
        userData.put("E-mail", req.queryParams("email"));
        User user = new User(userData);
        Order order = new Order(user);
        orderData.add(order);
    }

    public static boolean checkLogin(Request req, Response res) {
        if (userDataJDBC.getPassByEmail(req.queryParams("email")).equals("")) return false;
        return com.codecool.shop.Password.checkPassword(req.queryParams("password"), userDataJDBC.getPassByEmail(req.queryParams("email")));
    }

    public static int getUserIdByEmail(String email) {
        return userDataJDBC.getUserId(email);
    }

    public static boolean check(Request req) {
        return userDataJDBC.emailCheck(req.queryParams("email"));
    }

}
