package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import org.junit.Before;
import org.junit.jupiter.api.Test;

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
         instance.getAll()
    }
}

class cartDaoMemTests extends CartDaoTest<CartDaoMem> {

    @Override
    protected CartDaoMem createInstance(){
        return new CartDaoMem();
    }

}