import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class OrderDaoTest<T extends OrderDao> {

    protected T instance;

    protected abstract T createInstance();

    protected abstract void setUpInstance();

    protected abstract Order getSampleOrder();

    @BeforeEach
    public void setUp() {
        setUpInstance();
    }

    @Test
    public void testEmptyOrderList() {
        TestDataMem.clearInstances();
        TestDataJDBC.executeSqlScript(new File("tests/resetOrders.sql"));
        ArrayList<Order> expected = new ArrayList <>();
        assertTrue(instance.getAll().equals(expected));
    }

    @Test
    public void testAddMethodAddedOneOrder() {
        int size = instance.getAll().size();
        Order added = getSampleOrder();
        instance.add(added);
        assertTrue(instance.getAll().size() == size+1);
    }

    @Test
    public void testGetAllMethod() {
        assertTrue(instance.getAll().size() == 1);
    }
    @Test
    public void testGetLastMethod() {
        /*Order expected = getSampleOrder();
        expected.setId(1);
        assertEquals(instance.getLast(), expected);*/
        assertTrue(instance.getLast().getClass()== Order.class);
    }

}

class OrderDaoMemTest extends OrderDaoTest<OrderDaoMem> {

    @Override
    protected OrderDaoMem createInstance() {
        return OrderDaoMem.getInstance();
    }

    @Override
    protected void setUpInstance(){
        instance = createInstance();
        TestDataMem.fillInstances();
    }

    @Override
    protected Order getSampleOrder() {
        CartDaoMem cartDaoMem = CartDaoMem.getInstance();

        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        List<String> data = new ArrayList<>(Arrays.asList("Gipsz Jakab", "testemail@gmail.com", "303377027", "Kőbányai utca", "Budakalász", "2011", "Hungary","Déryné utca", "Gödöllő", "2100",  "Hungary"));
        LinkedHashMap userData = new LinkedHashMap();
        for (int i=0; i<list.size(); i++){
            userData.put(list.get(i), data.get(i));
        };
        return new Order(cartDaoMem.getAll(), userData);
    }
}

class OrderDaoJDBCTest extends OrderDaoTest<OrderDaoJDBC> {

    @Override
    protected OrderDaoJDBC createInstance(){
        return OrderDaoJDBC.getInstance();
    }

    @Override
    protected void setUpInstance() {
        instance = createInstance();
        TestDataJDBC.executeSqlScript(new File("tests/reset_data.sql"));
    }

    @Override
    protected Order getSampleOrder(){
        Map<Product,Integer> cart = new HashMap <>();

        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");

        Product product1 = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon);
        Product product2 = new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo);
        Product product3 = new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon);

        cart.put(product1,2);
        cart.put(product2,1);
        cart.put(product3,1);

        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        List<String> data = new ArrayList<>(Arrays.asList("Gipsz Jakab", "testemail@gmail.com", "303377027", "Kőbányai utca", "Budakalász", "2011", "Hungary","Déryné utca", "Gödöllő", "2100",  "Hungary"));
        LinkedHashMap userData = new LinkedHashMap();
        for (int i=0; i<list.size(); i++){
            userData.put(list.get(i), data.get(i));
        };
        return new Order(cart, userData);
    }

}