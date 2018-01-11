import com.codecool.shop.dao.implementation.SupplierDaoMem;

class SupplierDoaMemTest extends SupplierDaoTest<SupplierDaoMem> {

    @Override
    protected SupplierDaoMem createInstance() {
        return SupplierDaoMem.getInstance();
    }

}