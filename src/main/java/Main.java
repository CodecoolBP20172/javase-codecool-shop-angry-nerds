import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IllegalArgumentException{

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8000);

        // populate some data for the memory storage
        populateData();

        before("/protected/*", (request, response) -> {
            if (request.session().attribute("email") == null) {
                halt(501, "Login for this page");
            }
            });

        // Always add generic routes to the end
        get("/", ProductController::renderProducts, new ThymeleafTemplateEngine());
        // Equivalent with above
        get("/index", (Request req, Response res) -> {
           return new ThymeleafTemplateEngine().render( ProductController.renderProducts(req, res) );
        });
        get("/listby/:supOrCat/:id", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render( ProductController.renderProductsBy(req ,req.params(":supOrCat"), req.params(":id")) );
        });
        get("/protected/cart/:id", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render( ProductController.addProduct(req,req.params(":id")) );
        });
        get("/protected/showcart", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render( ProductController.renderCart(req));
        });
        get("/protected/remove/:id", (Request req, Response res) -> {
            ProductController.removeProduct(req, Integer.parseInt(req.params(":id")));
            return new ThymeleafTemplateEngine().render( ProductController.renderCart(req));
        });
        get("/protected/changeQuantity/:id", (Request req, Response res) -> {
            ProductController.changeQuantity(req,Integer.parseInt(req.params(":id")),Integer.parseInt(req.queryParams("quantity")));
            return new ThymeleafTemplateEngine().render( ProductController.renderCart(req));
        });



        get("/protected/checkout", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.forms("Checkout", req));
        });

        post("/protected/payment", (Request req, Response res) -> {
            ProductController.saveData(req);
            return new ThymeleafTemplateEngine().render(ProductController.forms("Payment", req.queryParams("payment"), req));
        });

        post("/protected/confirm", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.confirmation(req));
        });

        get("/login", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.login(req, res));
        });

        get("/sign-up", (Request req, Response res) -> {
            return new ThymeleafTemplateEngine().render(ProductController.signUp(req, res));
        });

        post("/login", (Request req, Response res) -> {
            //check login here
            if (ProductController.checkLogin(req, res)) {
                req.session(true);
                req.session().attribute("email",req.queryParams("email"));
                int userId = ProductController.getUserIdByEmail(req.queryParams("email"));
                req.session().attribute("userId", String.valueOf(userId));
                res.redirect("/");
            } else {
                halt(401, "Invalid login");
                res.redirect("/");
            }
            return null;
        });

        post("/sign-up", (Request req, Response res) -> {
            //save user data here
            if (ProductController.check(req)){
                req.session(true);
                req.session().attribute("email",req.queryParams("email"));
                ProductController.saveUser(req, res);
                res.redirect("/login");
            } else {
                res.redirect("/sign-up");
            }
            return null;
        });

        get("/logout", (Request req, Response res) -> {
            req.session().removeAttribute("email");
            res.redirect("/");
            return null;
                });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();

    }

    public static void populateData() throws IllegalArgumentException {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);

        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory notebook = new ProductCategory("Notebook", "Hardware", "A portable computer.");
        productCategoryDataStore.add(notebook);

        //setting up products and printing it

        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
    }


}
