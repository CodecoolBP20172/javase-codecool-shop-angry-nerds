package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Product;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CartDaoTest<T extends CartDao> {

    private T instance;

    protected abstract T createInstance();

    @Before
    public void setUp() {
        instance = createInstance();
    }

    @Test
    public void addIfArgNull() {
        Map<Product, Integer> mapBeforeAdd = new HashMap<>();
        mapBeforeAdd.putAll(instance.getAll());
        instance.add(null);
        assertEquals(mapBeforeAdd, instance.getAll());
    }
}

class cartDaoMemTests extends CartDaoTest<CartDaoMem> {

    @Override
    protected CartDaoMem createInstance(){
        return new CartDaoMem();
    }

}