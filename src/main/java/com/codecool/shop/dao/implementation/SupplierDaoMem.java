package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The data access object of the Supplier class which helps to save and load data from the memory.
 * <p>
 * It implements the SupplierDao interface, which has 4 methods, one, that adds a Supplier to an ArrayList
 * representing the memory. One, that removes it if necessary, one, that returns all categories in a list
 * and one for searching for a supplier in the memory.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class SupplierDaoMem implements SupplierDao {

    private List<Supplier> DATA = new ArrayList<>();
    private static SupplierDaoMem instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoMem.class);

    /**
     * This method checks if an instance exists already. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, then the getInstance method
     * returns this. The result is that the returned value is always the same instance, and no new resource
     * or overhead is required.
     * @return a single instance of the class
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

    /**
     * This method adds a Supplier to an ArrayList, which represents the memory. It has
     * one parameter, which is a Supplier that gets inserted into the list. If the argument
     * is null it throws an IllegalArgumentException.
     * @param supplier this is the Supplier that gets added to the memory
     * @throws IllegalArgumentException if the parameter supplier equals to null
     */
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

    /**
     * This method searches for the Supplier in the memory based on its id.
     * If it does not find the Supplier, it returns null. However if the Supplier is
     * found based on the id than it gets returned.
     * @param id the id of the searched Supplier
     * @return the Supplier which has the corresponding id
     */
    @Override
    public Supplier find(int id) {
        logger.debug("Supplier found in memory and returned");
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * This method finds and removes the Supplier based on the id given as parameter.
     * @param id the Supplier will be removed based on this id
     */
    @Override
    public void remove(int id) {
        DATA.remove(find(id));
        logger.debug("Supplier removed from the memory");
    }

    /**
     * This method gets all the suppliers from the memory and returns them in an ArrayList.
     * @return an ArrayList, which contains all suppliers
     */
    @Override
    public List<Supplier> getAll() {
        logger.debug("Returning all suppliers stored in memory");
        return DATA;
    }
}
