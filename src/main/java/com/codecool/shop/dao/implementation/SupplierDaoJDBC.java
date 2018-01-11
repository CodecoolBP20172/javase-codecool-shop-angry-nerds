package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao{

    private static SupplierDaoJDBC instance = null;

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) throws IllegalArgumentException {
        if (supplier == null) throw new IllegalArgumentException();
        String query = "INSERT INTO supplier (name, description) VALUES (?,?);";
        ArrayList<TypeCaster> queryList = new ArrayList<>();

        queryList.add(new TypeCaster(supplier.getName(), false));
        queryList.add(new TypeCaster(supplier.getDescription(), false));
        try(ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute(query, queryList);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Supplier find(int id) {
        String query = "SELECT * FROM supplier WHERE id = ?;";
        List<Supplier> categoryList = new ArrayList<>();
        
        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(id), true));
            ResultSet rs = conn.process(query, queryList);
            while (rs.next()) {
                Supplier supplier = new Supplier(rs.getString("name"),
                        rs.getString("description"));
                supplier.setId(rs.getInt("id"));
                categoryList.add(supplier);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList.get(0);
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM supplier WHERE id = ?;";

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> queryList = new ArrayList<>();
            queryList.add(new TypeCaster(String.valueOf(id), true));
            conn.execute(query, queryList);
        }
        catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return resultList;
    }
}
