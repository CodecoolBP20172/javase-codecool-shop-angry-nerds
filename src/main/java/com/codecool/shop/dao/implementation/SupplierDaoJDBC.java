package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a supplier data access object which helps the Supplier class to communicate with
 * the database.
 * <p>
 * It implements the SupplierDao interface. It has 4 methods, one, that adds a Supplier instance to the
 * database, one, that removes it if necessary, one, that returns all suppliers in a list and one for
 * searching for a supplier in the database.
 *
 * @author  Peter Bernath
 * @version 1.0
 * @since   2018-01-17
 */
public class SupplierDaoJDBC implements SupplierDao{

    private static SupplierDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJDBC.class);

    private SupplierDaoJDBC() {
    }

    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        logger.debug("SupplierDaoJDBC instance created");
        return instance;
    }

    /**
     * This method adds a Supplier to the database. It has one parameter, which is of
     * Supplier type, that gets inserted into the database. If the argument is null it throws
     * an IllegalArgumentException.
     * @param supplier this is the Supplier that gets added to the DB
     * @throws IllegalArgumentException if the parameter supplier equals to null
     */
    @Override
    public void add(Supplier supplier) throws IllegalArgumentException {
        if (supplier == null) {
            logger.debug("SupplierDaoJDBC add method received invalid argument");
            throw new IllegalArgumentException();
        }
        String query = "INSERT INTO supplier (name, description) VALUES (?,?);";
        ArrayList<TypeCaster> queryList = new ArrayList<>();

        queryList.add(new TypeCaster(supplier.getName(), false));
        queryList.add(new TypeCaster(supplier.getDescription(), false));
        try(ConnectionHandler conn = new ConnectionHandler()) {
            logger.debug("Supplier added successfully to database");
            conn.execute(query, queryList);
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while adding supplier to database");
            e.printStackTrace();
        }
    }

    /**
     * This method searches for the Supplier in the database based on its id.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure, if it doesn't find the id in the DB, it returns null.
     * However if the Supplier is found based on the id than it gets returned.
     * @param id the Supplier will be searched for based on this id
     * @return the Supplier which has the corresponding id
     */
    @Override
    public Supplier find(int id) {
        String query = "SELECT * FROM supplier WHERE id = ?;";
        List<Supplier> supplierList = new ArrayList<>();

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(id), true));
            ResultSet rs = conn.process(query, queryList);
            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getString("name"),
                        rs.getString("description"));
                supplier.setId(rs.getInt("id"));
                supplierList.add(supplier);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to find product category in database");
            e.printStackTrace();
        }
        if (supplierList.size() > 0) {
            logger.debug("Supplier found and returned");
            return supplierList.get(0);
        }
        else {
            logger.warn("Supplier not found in database");
            return null;
        }
    }

    /**
     * This method removes the Supplier that has the id given by the parameter.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the Supplier is found based on
     * the id, then it gets removed.
     * @param id the Supplier will be removed based on this id
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM supplier WHERE id = ?;";

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(id), true));
            conn.execute(query, queryList);
            logger.debug("Supplier removed from database");
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying to remove supplier from database");
        }
    }

    /**
     * This method gets all the suppliers from the database and returns them in a list.
     * <p>
     * It uses the ConnectionHandler class to establish connection to the database. If it fails, it
     * logs a warning about connection failure. However, if the connection went through without problems,
     * then it returns all the suppliers in an ArrayList.
     * @return an ArrayList, which contains all the suppliers
     */
    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM supplier;";
        ArrayList<Supplier> resultList = new ArrayList<>();

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet rs = conn.process(query);
            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getString("name"),
                        rs.getString("description"));
                supplier.setId(rs.getInt("id"));
                resultList.add(supplier);
            }
        }
        catch (SQLException e) {
            logger.warn("Connection to database failed while trying get all suppliers from database");
        }
        logger.debug("Returning all suppliers in a list");
        return resultList;
    }
}
