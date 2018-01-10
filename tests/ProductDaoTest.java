import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProductDaoTest<T extends ProductDao> {

    protected T instance;

    protected abstract T createInstance();

    protected abstract void setUpInstance();

    @BeforeEach
    public void setUp() {
        setUpInstance();
    }

    @Test
    public void testAdd() {

    }

    @Test
    public void testFind() {
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        amazon.setId(1);
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        tablet.setId(1);
        Product expected = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon);
        expected.setId(1);
        assertEquals(expected, instance.find(1));
    }

    @Test
    public void testTestFind() {
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        amazon.setId(1);
        ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        tablet.setId(1);
        Product expected = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon);
        expected.setId(1);
        assertEquals(expected, instance.find(4));
    }

    @Test
    public void testRemove() {

    }

    @Test
    public void testGetAll() {

    }

    @Test
    public void testGetBySupplier() {

    }

    @Test
    public void testGetByProductCategory() {

    }


}

class ProductDaoMemTest extends ProductDaoTest<ProductDaoMem> {

    @Override
    protected ProductDaoMem createInstance(){
        return ProductDaoMem.getInstance();
    }

    @Override
    public void setUpInstance(){
        TestDataMem.fillInstances();
        instance = createInstance();

    }

}

class ProductDaoJDBCTest extends ProductDaoTest<ProductDaoJDBC> {

    @Override
    protected ProductDaoJDBC createInstance(){
        return ProductDaoJDBC.getInstance();
    }

    @Override
    public void setUpInstance(){
        instance = createInstance();
    }

}