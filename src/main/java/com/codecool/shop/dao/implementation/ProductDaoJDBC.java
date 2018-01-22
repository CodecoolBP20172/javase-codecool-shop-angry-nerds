package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.database.ConnectionHandler;
import com.codecool.shop.database.TypeCaster;
import com.codecool.shop.model.BaseModel;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * This is a simple product data access object which serves the Product class and helpes it to
 * communicate with the database.
 * <p>
 * It implements the ProductDao interface.
 *
 * @author  Márton Ollári
 * @version 1.0
 * @since   2018-01-20
 */
public class ProductDaoJDBC implements ProductDao {
    private static ProductDaoJDBC instance = null;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);

    private ProductDaoJDBC() {
    }
    /**
     * This method checks if an instance does not already exist. If not, a new instance of the class is created and
     * assigned to the instance variable. If an existing instance is available, the getInstance method returns this.
     * The result is that the returned value is always the same instance, and no new resource or overhead is required.
     * @return a single instance of the class
     */
    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        logger.debug("ProductDaoJDBC instance created");
        return instance;
    }
    /**
     * This method adds a Product to the database. It has
     * one parameter, which is a Product that gets inserted into the database.
     * @param product the Product that gets added to the database
     */
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
            logger.debug("Product added to database");
        } catch (SQLException e) {
            logger.warn("Connection to database failed while adding product to database");
            e.printStackTrace();
        }
    }
    /**
     * This method searches for the Product in the database, based on the id of the product.
     * If it does not find the Product, it returns null. However if the Product is
     * found based on the id than it gets returned.
     * @param id the Product will be searched for based on this id
     * @return the Product, which has the corresponding id
     */
    @Override
    public Product find(int id) {
        Product product = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(id), true));
            ResultSet rs = con.process("SELECT * FROM product WHERE id = ?", list);
            List<Product> productList = getProductsFromResultSet(rs);
            product = productList.get(0);
            logger.debug("Product found in database by id {}", id);
        } catch (SQLException e) {
            logger.warn("Connection to database failed while finding product from database");
            e.printStackTrace();
        }
        return product;
    }
    /**
     * This method finds and removes the Product from the database, based on the id given as parameter.
     * @param id the Product will be removed based on this id
     */
    @Override
    public void remove(int id) {
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(id), true));
            con.process("DELETE FROM product WHERE id = ?", list);
            logger.debug("Product removed from database by id {}", id);
        } catch (SQLException e) {
            logger.warn("Connection to database failed while removing product from database");
            e.printStackTrace();
        }
    }
    /**
     * This method gets all the products from the memory and returns them in an ArrayList.
     * Use {@link #getProductsFromResultSet(ResultSet)} to change the result set into an ArrayList
     * @return an ArrayList, which contains all the products
     */
    @Override
    public List<Product> getAll() {
        List<Product> productList = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            ResultSet rs = con.process("SELECT * FROM product");
            productList = getProductsFromResultSet(rs);
            logger.debug("Getting all products from database");
        } catch (SQLException e) {
            logger.warn("Connection to database failed while getting supplier all products");
            e.printStackTrace();
        }
        return productList;
    }
    /**
     * This method gets all the products from the database which has the same supplier,
     * as the supplier given as a parameter, and returns them in an ArrayList.
     * Use {@link #getProductBySupOrPC(BaseModel, String)}
     * @param  supplier the Supplier to get the Products
     * @return an ArrayList, which contains all the products with the same supplier
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        List<Product> productList = getProductBySupOrPC(supplier, "supplier_id");
        logger.debug("Getting all products from database by supplier {}", supplier.getName());
        return productList;
    }
    /**
     * This method gets all the products from the database which has the same category,
     * as the category given as a parameter, and returns them in an ArrayList.
     * Use {@link #getProductBySupOrPC(BaseModel, String)}
     * @param  productCategory the ProductCategory to get the Products
     * @return an ArrayList, which contains all the products with the same supplier
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        List<Product> productList = getProductBySupOrPC(productCategory, "product_category_id");
        logger.debug("Getting all products from database by product category {}", productCategory.getName());
        return productList;
    }
    /**
     * This method gets all the products from the database which has the same category,
     * as the category given as a parameter, and returns them in an ArrayList.
     * @param  rs a ResultSet from the database
     * @return an ArrayList of Products from the ResultSet
     */
    private List<Product> getProductsFromResultSet(ResultSet rs) throws SQLException {
        List<Product> productList = new ArrayList<>();
        while (rs.next()){
            String name = rs.getString("name");
            float default_price = rs.getFloat("default_price");
            String default_currency = rs.getString("default_currency");
            String description = rs.getString("description");
            int  product_category_id = rs.getInt("product_category_id");
            int supplier_id = rs.getInt("supplier_id");
            int product_id = rs.getInt("id");
            Product product = new Product(name, default_price, default_currency, description, ProductCategoryDaoJDBC.getInstance().find(product_category_id), SupplierDaoJDBC.getInstance().find(supplier_id));
            product.setId(product_id);
            productList.add(product);
        }
        return productList;
    }
    /**
     * This method gets all the products from the database which has the same category,
     * as the category given as a parameter, and returns them in an ArrayList.
     * Use {@link #getProductsFromResultSet(ResultSet)} to change the result set into an ArrayList
     * @param  model a Supplier or ProductCategory to get the Products
     * @param  supOrPC a String for the query to get the product from the database
     * @return an ArrayList, which contains all the products with the same supplier
     */
    private List<Product> getProductBySupOrPC(BaseModel model, String supOrPC){
        List<Product> productList = null;
        try (ConnectionHandler con = new ConnectionHandler()){
            List<TypeCaster> list = new ArrayList<>();
            list.add(new TypeCaster(Integer.toString(model.getId()), true));
            ResultSet rs;
            if (supOrPC == "supplier_id"){
                rs = con.process("SELECT * FROM product WHERE supplier_id = ?", list);
            } else {
                rs = con.process("SELECT * FROM product WHERE product_category_id = ?", list);
            }
            productList = getProductsFromResultSet(rs);
        } catch (SQLException e) {
            logger.warn("Connection to database failed while getting all products by supplier/category from database");
            e.printStackTrace();
        }
        return productList;
    }
}
