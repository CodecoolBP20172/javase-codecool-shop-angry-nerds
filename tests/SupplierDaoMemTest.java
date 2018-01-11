import com.codecool.shop.dao.implementation.SupplierDaoMem;

class SupplierDaoMemTest extends SupplierDaoTest<SupplierDaoMem> {

    @Override
    protected SupplierDaoMem createInstance() {
        return SupplierDaoMem.getInstance();
    }

}