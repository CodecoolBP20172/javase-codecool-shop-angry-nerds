package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static ProductDaoJDBC instance = null;

    private ProductDaoJDBC() {
    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(product.getName(), false));
            list.add(new TypeCaster(Float.toString(product.getDefaultPrice()), true));
            list.add(new TypeCaster(product.getDefaultCurrency().toString(), false));
            list.add(new TypeCaster(product.getDescription(), false));
            list.add(new TypeCaster(Integer.toString(product.getProductCategory().getId()), true));
            list.add(new TypeCaster(Integer.toString(product.getSupplier().getId()), true));

            con.process("INSERT INTO product VALUES (DEFAULT, ?, ?, ?,  ?, ?, ?);", list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product find(int id) {
        Product product = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(id), true));
            ResultSet rs = con.process("SELECT * FROM product WHERE id = ?", list);
            List<Product> productList = getProductsFromResultSet(rs);
            product = productList.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void remove(int id) {
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(id), true));
            con.process("DELETE FROM product WHERE id = ?", list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        List<Product> productList = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            ResultSet rs = con.process("SELECT * FROM product");
            productList = getProductsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> productList = getProductBySupOrPC(supplier, "supplier_id");
        return productList;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> productList = getProductBySupOrPC(productCategory, "product_category_id");
        return productList;
    }

    private List<Product> getProductsFromResultSet(ResultSet rs) throws SQLException {
        List<Product> productList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            float default_price = rs.getFloat("default_price");
            String default_currency = rs.getString("default_currency");
            String description = rs.getString("description");
            int  product_category_id = rs.getInt("product_category_id");
            int supplier_id = rs.getInt("supplier_id");
            productList.add(new Product(name, default_price, default_currency, description, ProductCategoryDaoJDBC.getInstance().find(product_category_id), SupplierDaoJDBC.getInstance().find(supplier_id)));
        }
        return productList;
    }

    private List<Product> getProductBySupOrPC(BaseModel model, String supOrPC){
        List<Product> productList = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(model.getId()), true));
            ResultSet rs = null;
            if (supOrPC == "supplier_id"){
                rs = con.process("SELECT * FROM product WHERE supplier_id = ?", list);
            } else {
                rs = con.process("SELECT * FROM product WHERE product_category_id = ?", list);
            }
            productList = getProductsFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
