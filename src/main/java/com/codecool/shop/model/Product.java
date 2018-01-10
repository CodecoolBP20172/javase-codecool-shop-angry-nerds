package com.codecool.shop.model;

import java.util.Currency;

public class Product extends BaseModel {

    private float defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;


    public Product(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }

    public void setPrice(float price, String currency) {
        this.defaultPrice = price;
        this.defaultCurrency = Currency.getInstance(currency);
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }

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
