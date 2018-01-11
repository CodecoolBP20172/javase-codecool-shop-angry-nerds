import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProductCategoryDaoTest<T extends ProductCategoryDao> {

    private T instance = createInstance();

    protected abstract T createInstance();

//    ProductCategory notebook = new ProductCategory("Notebook", "Hardware", "A portable computer.");
//    ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
//    ProductCategory audio = new ProductCategory("Audio", "Hardware", "bla bla");
//    ProductCategory electronics = new ProductCategory("Electronics", "Hardware", "bla bla");


    @BeforeEach
    void initialize() {
        TestDataMem.clearInstances();
        TestDataMem.fillInstances();
    }

    @Test
    void testAdd() throws IllegalArgumentException {
        instance.add(TestDataMem.tablet);
        assertTrue(instance.getAll().contains(TestDataMem.tablet));
        assertThrows(IllegalArgumentException.class, () -> {
            instance.add(null);
        });
        instance.remove(TestDataMem.tablet.getId());
    }

    @Test
    void find() {
        assertEquals(TestDataMem.notebook,instance.find(TestDataMem.notebook.getId()));
        assertEquals(TestDataMem.audio,instance.find(TestDataMem.audio.getId()));
    }
    @Test
    void remove() {
        if (instance.getAll().contains(TestDataMem.notebook)) instance.remove(TestDataMem.notebook.getId());
        assertFalse(instance.getAll().contains(TestDataMem.notebook));
    }

    @Test
    void getAll() {
        assertTrue(instance.getAll().size() == 4);
    }

}