package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SupplierDaoMem implements SupplierDao {

    private List<Supplier> DATA = new ArrayList<>();
    private static SupplierDaoMem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoMem.class);

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoMem() {
    }

    public static SupplierDaoMem getInstance() {
        if (instance == null) {
            instance = new SupplierDaoMem();
        }
        logger.debug("SupplierDaoMem instance created");
        return instance;
    }

    @Override
    public void add(Supplier supplier) throws IllegalArgumentException{
        if (supplier == null) {
            logger.debug("SupplierDaoMem  add method received invalid argument");
            throw new IllegalArgumentException();
        }
        supplier.setId(DATA.size() + 1);
        DATA.add(supplier);
        logger.debug("Supplier added successfully to the memory");
    }

    @Override
    public Supplier find(int id) {
        logger.debug("Supplier found in memory and returned");
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.debug("Supplier removed from the memory");
    }

    @Override
    public List<Supplier> getAll() {
        logger.debug("Returning all suppliers stored in memory");
        return DATA;
    }
}
