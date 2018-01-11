import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;

class ProductCategoryDaoMemTest extends ProductCategoryDaoTest<ProductCategoryDaoMem> {

    @Override
    protected ProductCategoryDaoMem createInstance() {
        return ProductCategoryDaoMem.getInstance();
    }

}