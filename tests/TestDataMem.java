import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class TestDataMem {

    private static ProductDaoMem productDaoMem = ProductDaoMem.getInstance();
    private static SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();
    private static ProductCategoryDaoMem productCategoryDaoMem = ProductCategoryDaoMem.getInstance();
    private static OrderDaoMem orderDaoMem = OrderDaoMem.getInstance();
    private static CartDaoMem cartDaoMem = CartDaoMem.getInstance();

    protected static void fillInstances() {
        clearInstances();
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        ProductCategory notebook = new ProductCategory("Notebook", "Hardware", "A portable computer.");

        Product product1 = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon);
        Product product2 = new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo);
        Product product3 = new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon);
        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        List<String> data = new ArrayList<>(Arrays.asList("Gipsz Jakab", "testemail@gmail.com", "303377027", "Kőbányai utca", "Budakalász", "2011", "Hungary","Déryné utca", "Gödöllő", "2100",  "Hungary"));
        LinkedHashMap userData = new LinkedHashMap();
        for (int i=0; i<list.size(); i++){
            userData.put(list.get(i), data.get(i));
        };

        supplierDaoMem.add(amazon);
        supplierDaoMem.add(lenovo);

        productCategoryDaoMem.add(tablet);
        productCategoryDaoMem.add(notebook);

        productDaoMem.add(product1);
        productDaoMem.add(product2);
        productDaoMem.add(product3);

        cartDaoMem.add(product1);
        cartDaoMem.add(product1);
        cartDaoMem.add(product2);
        cartDaoMem.add(product3);

        Order order = new Order(cartDaoMem.getAll(), userData);

        orderDaoMem.add(order);

    }

    protected static void clearInstances() {
       productDaoMem.getAll().clear();
       supplierDaoMem.getAll().clear();
       productCategoryDaoMem.getAll().clear();
       orderDaoMem.getAll().clear();
       cartDaoMem.getAll().clear();
    }

}
