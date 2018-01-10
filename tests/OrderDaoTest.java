import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class OrderDaoTest<T extends OrderDao> {

    protected T instance;

    protected abstract T createInstance();

    protected abstract void setUpInstance();

    @BeforeEach
    public void setUp() {
        setUpInstance();
    }

    @Test
    public void testEmptyOrderList() {
        ArrayList<Order> expected = new ArrayList <>();
        assertTrue(instance.getAll().equals(expected));
    }

    @Test
    public void testAddMethod() {
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

    }

}