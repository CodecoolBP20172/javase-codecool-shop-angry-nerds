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

public class SupplierDaoJDBC implements SupplierDao{

    private static SupplierDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJDBC.class);

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        logger.debug("SupplierDaoJDBC instance created");
        return instance;
    }

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
