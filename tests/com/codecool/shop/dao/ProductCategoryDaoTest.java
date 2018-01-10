package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProductCategoryDaoTest<T extends ProductCategoryDao> {

    private T instance = createInstance();

    protected abstract T createInstance();

    ProductCategory notebook = new ProductCategory("Notebook", "Hardware", "A portable computer.");
    ProductCategory tablet = new ProductCategory("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
    ProductCategory audio = new ProductCategory("Audio", "Hardware", "bla bla");
    ProductCategory electronics = new ProductCategory("Electronics", "Hardware", "bla bla");


    @BeforeEach
    void initialize() {
        instance.add(notebook);
        instance.add(audio);
        instance.add(electronics);
    }

    @Test
    void testAdd() throws IllegalArgumentException {
        instance.add(tablet);
        assertTrue(instance.getAll().contains(tablet));
        assertThrows(IllegalArgumentException.class, () -> {
            instance.add(null);
        });
        instance.remove(tablet.getId());
    }

    @Test
    void find() {
        assertEquals(notebook,instance.find(notebook.getId()));
        assertEquals(audio,instance.find(audio.getId()));
    }

    @Test
    void remove() {
        instance.remove(notebook.getId());
        assertFalse(instance.getAll().contains(notebook));
    }

    @Test
    void getAll() {
        assertTrue(instance.getAll().size() == 3);
    }

    @AfterEach
    void finish() {
        if(instance.getAll().contains(notebook)) instance.remove(notebook.getId());
        instance.remove(audio.getId());
        instance.remove(electronics.getId());
    }
}