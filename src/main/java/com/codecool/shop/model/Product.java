package com.codecool.shop.model;

import java.util.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This class serves as model for every product and holds all the information about each of them.
 * It extends the BaseModel abstract class from which it inherits its base fields like id, name and description,
 * to which four more class specific fields are added, the default price, default currency, product category
 * and supplier
 *
 * @author  Márton Ollári
 * @version 1.0
 * @since   2018-01-20
 */

public class Product extends BaseModel {

    private float defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;
    private static final Logger logger = LoggerFactory.getLogger(Product.class);

    /**
     * The constructor of the Product class
     * @param name the name of the product
     * @param defaultPrice the default price of the product
     * @param currencyString the currency of the product
     * @param description a base description of the product
     * @param productCategory the category of the product
     * @param supplier the supplier of the product
     */
    public Product(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
        logger.trace("Created instance of class Product with name {} and description {}", this.name, this.description);

    }
    /**
     * Gets the default price of the product
     * @return the default price of the product
     */
    public float getDefaultPrice() {
        logger.trace("Product {} default price is {}", this.name, this.defaultPrice);
        return defaultPrice;
    }
    /**
     * Sets the default price of the product based on its parameter
     * @param defaultPrice sets default price of the product
     */
    public void setDefaultPrice(float defaultPrice) {
        logger.trace("Set products default price {} to {}", this.defaultPrice, defaultPrice);
        this.defaultPrice = defaultPrice;
    }
    /**
     * Gets the default currency of the product
     * @return the default currency of the product
     */
    public Currency getDefaultCurrency() {
        logger.trace("Product {} default currency is {}", this.name, this.defaultCurrency);
        return defaultCurrency;
    }
    /**
     * Sets the default currency of the product based on its parameter
     * @param defaultCurrency sets default currency of the product
     */
    public void setDefaultCurrency(Currency defaultCurrency) {
        logger.trace("Set products default currency {} to {}", this.defaultCurrency, defaultCurrency);
        this.defaultCurrency = defaultCurrency;
    }
    /**
     * Gets the price of the product
     * @return the price (a String with default price and currency) of the product
     */
    public String getPrice() {
        logger.trace("Product {} price is {} {}", this.name, this.defaultPrice, this.defaultCurrency);
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }
    /**
     * Sets the price of the product based on its parameter
     * @param price sets default price of the product
     * @param currency sets the currency of the product
     */
    public void setPrice(float price, String currency) {
        this.defaultPrice = price;
        this.defaultCurrency = Currency.getInstance(currency);
        logger.trace("Set products price {} {} to {} {}", this.defaultPrice, this.defaultPrice, price, currency);

    }
    /**
     * Gets the product category of the product
     * @return the product category of the product
     */
    public ProductCategory getProductCategory() {
        logger.trace("Product {} product category is {}", this.name, this.productCategory.toString());
        return productCategory;
    }
    /**
     * Sets the product category of the product based on its parameter
     * @param productCategory sets the product category of the product to this ProductCategory
     */
    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
        logger.trace("Set product category {} to {}", this.productCategory.getName(), productCategory.getName());

    }
    /**
     * Gets the supplier of the product
     * @return the supplier of the product
     */
    public Supplier getSupplier() {
        logger.trace("Product {} supplier is {}", this.name, this.supplier.toString());
        return supplier;
    }
    /**
     * Sets the supplier of the product based on its parameter
     * @param supplier sets the supplier of the product to this Supplier
     */
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
        logger.trace("Set supplier {} to {}", this.supplier.getName(), supplier.getName());

    }
    /**
     * Sets a custom toString representation of the class
     * @return the new string format of the Product class
     */
    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "defaultPrice: %3$f, " +
                        "defaultCurrency: %4$s, " +
                        "productCategory: %5$s, " +
                        "supplier: %6$s",
                this.id,
                this.name,
                this.defaultPrice,
                this.defaultCurrency.toString(),
                this.productCategory.getName(),
                this.supplier.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Float.compare(product.getDefaultPrice(), getDefaultPrice()) != 0) return false;
        if (getDefaultCurrency() != null ? !getDefaultCurrency().equals(product.getDefaultCurrency()) : product.getDefaultCurrency() != null)
            return false;
        if (getProductCategory() != null ? !getProductCategory().equals(product.getProductCategory()) : product.getProductCategory() != null)
            return false;
        return getSupplier() != null ? getSupplier().equals(product.getSupplier()) : product.getSupplier() == null;
    }

    @Override
    public int hashCode() {
        int result = (getDefaultPrice() != +0.0f ? Float.floatToIntBits(getDefaultPrice()) : 0);
        result = 31 * result + (getDefaultCurrency() != null ? getDefaultCurrency().hashCode() : 0);
        result = 31 * result + (getProductCategory() != null ? getProductCategory().hashCode() : 0);
        result = 31 * result + (getSupplier() != null ? getSupplier().hashCode() : 0);
        return result;
    }
}
