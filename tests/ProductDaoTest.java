import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.dao.implementation.ProductDaoMem;
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
    public void testEmtyData() {

    }



}

class ProductDaoMemTest extends ProductDaoTest<ProductDaoMem> {

    @Override
    protected ProductDaoMem createInstance(){
        return ProductDaoMem.getInstance();
    }

    @Override
    public void setUpInstance(){
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