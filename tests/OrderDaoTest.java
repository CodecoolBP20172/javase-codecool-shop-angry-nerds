import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class OrderDaoTest<T extends OrderDao> {

    protected T instance;

    protected abstract T createInstance();

    protected abstract void setUpInstance();

    private Order getSampleOrder() {
        CartDaoMem cartDaoMem = CartDaoMem.getInstance();

        List<String> list = new ArrayList<>(Arrays.asList("Name", "E-mail", "Phone Number", "Billing Address", "Billing City", "Billing Zipcode", "Billing Country","Shipping Address", "Shipping City", "Shipping Zipcode",  "Shipping Country"));
        List<String> data = new ArrayList<>(Arrays.asList("Gipsz Jakab", "testemail@gmail.com", "303377027", "Kőbányai utca", "Budakalász", "2011", "Hungary","Déryné utca", "Gödöllő", "2100",  "Hungary"));
        LinkedHashMap userData = new LinkedHashMap();
        for (int i=0; i<list.size(); i++){
            userData.put(list.get(i), data.get(i));
        };
        return new Order(cartDaoMem.getAll(), userData);
    }

    @BeforeEach
    public void setUp() {
        setUpInstance();
        TestDataMem.fillInstances();
    }

    @Test
    public void testEmptyOrderList() {
        TestDataMem.clearInstances();
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
        Order expected = getSampleOrder();
        expected.setId(1);
        assertEquals(instance.getLast(), expected);
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
    }

}