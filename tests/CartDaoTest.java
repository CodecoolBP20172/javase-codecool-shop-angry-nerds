

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.implementation.CartDaoJDBC;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CartDaoTest<T extends CartDao> {

    protected T instance;

    protected abstract T createInstance();

    protected abstract void setUpInstance();

    private Random random = new Random();

    @BeforeEach
    public void setUp() {
        setUpInstance();
    }

    @Test
    public void addIfArgNullTest() {
        assertThrows(IllegalArgumentException.class, () -> instance.add(null));
    }

    @Test
    public void addTest() {
        List<Product> products = new ArrayList<>(instance.getAll().keySet());
        for (int i = 0; i < 100; i++) {
            Product product = products.get(random.nextInt(products.size()));
            if (i == 0) {
                product = new Product("test", 1, "USD", "test descr",

                        new ProductCategory("testCat", "testDep", "testing"), new Supplier("testSupp", "supp"));
            }

            int oldValue = instance.getAll().get(product) == null ? 0 : instance.getAll().get(product);
            instance.add(product);
            assertEquals((int) instance.getAll().get(product), oldValue + 1);
        }
    }

    @Test
    public void getAll() {
        assertEquals(3, instance.getAll().size());
    }

    @Test
    public void getAllWithAdd() {
        Map<Product, Integer> expectedMap = new HashMap<>(instance.getAll());
        List<Product> products = ProductDaoJDBC.getInstance().getAll();

        for (int i = 0; i < 20; i++) {
            Product product = products.get(random.nextInt(products.size()));
            instance.add(product);
            if (expectedMap.containsKey(product)) {
                expectedMap.put(product, expectedMap.get(product) + 1);
            } else {
                expectedMap.put(product, 1);
            }
        }
        assertEquals(expectedMap, instance.getAll());
    }

    @Test
    public void clear() {
        Map expectedMap = new HashMap();
        List<Product> products = new ArrayList<>(instance.getAll().keySet());
        while (random.nextBoolean()) {
            instance.add(products.get(random.nextInt(products.size())));
        }
        instance.clearCart();
        assertEquals(expectedMap, instance.getAll());

    }

    @Test
    public void removeIfWrongArg() {
        assertThrows(IllegalArgumentException.class, () -> instance.remove(0));
    }

    @Test
    public void removeOne() {
        Map<Product, Integer> expectedMap = new HashMap<>(instance.getAll());
        Map<Product, Integer> IterateExpectedMap = new HashMap<>(instance.getAll());

        for (Product product: IterateExpectedMap.keySet()){
            if (product.getId() == 3) {
                expectedMap.remove(product);
            }
        }
        instance.remove(3);

        assertEquals(expectedMap, instance.getAll());
    }

    @Test
    public void removeThree() {
        Map<Product, Integer> expectedMap = new HashMap<>(instance.getAll());
        Map<Product, Integer> IterateExpectedMap = new HashMap<>(instance.getAll());

        for (Product product: IterateExpectedMap.keySet()){
            if (product.getId() == 3 || product.getId() == 0 || product.getId() == 1 ) {
                expectedMap.remove(product);
            }
        }
        instance.remove(3);
        instance.remove(0);
        instance.remove(1);
        assertEquals(expectedMap, instance.getAll());
    }

    @Test
    public void setQuantityIfWrongId() {
        assertThrows(IllegalArgumentException.class, () -> instance.setQuantity(44,2));
    }

    @Test
    public void setQuantityIfWrongQuantity() {
        assertThrows(IllegalArgumentException.class, () -> instance.setQuantity(3,-2));
    }

    @Test
    public void setQuantityToZero() {
        Map<Product, Integer> expectedMap = new HashMap<>(instance.getAll());
        Map<Product, Integer> IterateExpectedMap = new HashMap<>(instance.getAll());

        for (Product product: IterateExpectedMap.keySet()){
            if (product.getId() == 3) {
                expectedMap.remove(product);
            }
        }
        instance.setQuantity(3,0);
        assertEquals(expectedMap, instance.getAll());
    }

    @Test
    public void setQuantity() {
        Map<Product, Integer> expectedMap = new HashMap<>(instance.getAll());
        for (Product product: expectedMap.keySet()){
            if (product.getId() == 2) {
                expectedMap.put(product,6);
            }
        }
        instance.setQuantity(2,6);
        assertEquals(expectedMap,instance.getAll());
    }

}

class cartDaoMemTests extends CartDaoTest<CartDaoMem> {

    @Override
    protected CartDaoMem createInstance() {
        return CartDaoMem.getInstance();
    }

    @Override
    public void setUpInstance(){
        TestDataMem.fillInstances();
        instance = createInstance();

    }

}

class cartDaoJdbcTests extends CartDaoTest<CartDaoJDBC> {

    @Override
    protected CartDaoJDBC createInstance() {
        return CartDaoJDBC.getInstance();
    }

    @Override
    protected void setUpInstance() {
        instance = createInstance();
        TestDataJDBC.executeSqlScript(new java.io.File("tests/reset_data.sql"));
    }
}
