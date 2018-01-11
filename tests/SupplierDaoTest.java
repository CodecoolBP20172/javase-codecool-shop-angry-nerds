import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SupplierDaoTest <T extends SupplierDao> {

    private T instance = createInstance();

    protected abstract T createInstance();

    @BeforeEach
    void initialize() {
        TestDataMem.clearInstances();
        TestDataMem.fillInstances();
        TestDataJDBC.executeSqlScript(new File("tests/reset_data.sql"));
    }

    @Test
    void testAdd() throws IllegalArgumentException {
        instance.add(TestDataMem.apple);
        assertTrue(instance.getAll().contains(TestDataMem.apple));
        assertThrows(IllegalArgumentException.class, () -> {
            instance.add(null);
        });
    }

    @Test
    void find() {
        assertEquals(TestDataMem.amazon,instance.find(TestDataMem.amazon.getId()));
        assertEquals(TestDataMem.lenovo,instance.find(TestDataMem.lenovo.getId()));
    }

    @Test
    void remove() {
        instance.remove(TestDataMem.amazon.getId());
        assertFalse(instance.getAll().contains(TestDataMem.amazon));
    }

    @Test
    void getAll() {
        assertTrue(instance.getAll().size() == 3);
    }
}