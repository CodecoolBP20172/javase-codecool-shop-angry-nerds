package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SupplierDaoTest <T extends SupplierDao> {

    private T instance = createInstance();

    protected abstract T createInstance();

    Supplier amazon = new Supplier("Amazon", "Digital content and services");
    Supplier lenovo = new Supplier("Lenovo", "Computers");
    Supplier apple = new Supplier("Apple", "Computers");


    @BeforeEach
    void initialize() {
        instance.add(amazon);
        instance.add(lenovo);
    }

    @Test
    void testAdd() throws IllegalArgumentException {
        instance.add(apple);
        assertTrue(instance.getAll().contains(apple));
        assertThrows(IllegalArgumentException.class, () -> {
            instance.add(null);
        });
        instance.remove(apple.getId());
    }

    @Test
    void find() {
        assertEquals(amazon,instance.find(amazon.getId()));
        assertEquals(lenovo,instance.find(lenovo.getId()));
    }

    @Test
    void remove() {
        instance.remove(amazon.getId());
        assertFalse(instance.getAll().contains(amazon));
    }

    @Test
    void getAll() {
        assertTrue(instance.getAll().size() == 2);
    }

    @AfterEach
    void finish() {
        if(instance.getAll().contains(amazon)) instance.remove(amazon.getId());
        instance.remove(lenovo.getId());
    }
}