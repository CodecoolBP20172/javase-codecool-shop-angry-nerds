import com.codecool.shop.dao.implementation.SupplierDaoJDBC;

class SupplierDaoJDBCTest extends SupplierDaoTest <SupplierDaoJDBC> {

    @Override
    protected SupplierDaoJDBC createInstance() {
        return SupplierDaoJDBC.getInstance();
    }

}