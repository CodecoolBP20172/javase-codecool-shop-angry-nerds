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

import java.util.*;

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
        for (int i = 0; i<100; i++) {
            Product product = randomProduct();
            int oldValue = instance.getAll().get(product) == null ? 0 : instance.getAll().get(product);
            instance.add(product);
            assertEquals((int) instance.getAll().get(product), oldValue + 1);
        }
    }

    @Test
    public void getAllIfEmpty() {
        assertEquals(0, instance.getAll().size());
    }

    @Test
    public void getAll() {
        Map expectedMap = new HashMap<Product, Integer>();
        for (int i = 0; i<100; i++) {
            Product product = randomProduct();
            instance.add(product);
            expectedMap.put(product, 1);
        }
        System.out.println(expectedMap);
        assertEquals(expectedMap, instance.getAll());
    }

}

class cartDaoMemTests extends CartDaoTest<CartDaoMem> {

    @Override
    protected CartDaoMem createInstance() {
        return CartDaoMem.getInstance();
    }
}
