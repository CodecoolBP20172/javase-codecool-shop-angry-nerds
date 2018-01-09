package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CartDaoTest<T extends CartDao> {

    private T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp() {
        instance = createInstance();
        instance.clearCart();
    }

    @Test
    public void addIfArgNullTest() {
        assertThrows(IllegalArgumentException.class, () -> instance.add(null));
    }

    @Test
    public void addTest() {
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        ProductCategory notebook = new ProductCategory("Notebook", "Hardware", "A portable computer.");
        Product product = new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.",
                notebook, lenovo);
        int oldValue = instance.getAll().get(product) == null ? 0: instance.getAll().get(product);
        instance.add(product);
        assertEquals((int) instance.getAll().get(product), oldValue+1);
    }

}

class cartDaoMemTests extends CartDaoTest<CartDaoMem> {

    @Override
    protected CartDaoMem createInstance() {
        return CartDaoMem.getInstance();
    }
}
