package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.ProductCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao{

    private static ProductCategoryDaoJDBC instance = null;

    private ProductCategoryDaoJDBC() {
    }

    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) throws IllegalArgumentException {
        if (category == null) throw new IllegalArgumentException();
        String query = "INSERT INTO product_category (name, description, department) VALUES (?,?,?);";
        ArrayList<TypeCaster> queryList = new ArrayList<>();

        queryList.add(new TypeCaster(category.getName(), false));
        queryList.add(new TypeCaster(category.getDescription(), false));
        queryList.add(new TypeCaster(category.getDepartment(), false));
        try(ConnectionHandler conn = new ConnectionHandler()) {
            conn.execute(query, queryList);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {
        String query = "SELECT * FROM product_category WHERE id = ?;";
        List<ProductCategory> categoryList = new ArrayList<>();
        
        try(ConnectionHandler conn = new ConnectionHandler()) {
            ArrayList<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(String.valueOf(id), true));
            ResultSet rs = conn.process(query, list);
            while (rs.next()) {
                ProductCategory category = new ProductCategory(rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("description"));
                category.setId(rs.getInt("id"));
                categoryList.add(category);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList.get(0);
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_category WHERE id = ?;";

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
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM product_category;";
        ArrayList<ProductCategory> resultList = new ArrayList<>();

        try(ConnectionHandler conn = new ConnectionHandler()) {
            ResultSet rs = conn.process(query);
            while (rs.next()) {
                ProductCategory category = new ProductCategory(rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("description"));
                category.setId(rs.getInt("id"));
                resultList.add(category);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
