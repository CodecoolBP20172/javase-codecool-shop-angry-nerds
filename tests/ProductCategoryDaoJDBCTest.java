import com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC;

class ProductCategoryDaoJDBCTest extends ProductCategoryDaoTest <ProductCategoryDaoJDBC> {

    @Override
    protected ProductCategoryDaoJDBC createInstance() {
        return ProductCategoryDaoJDBC.getInstance();
    }

}