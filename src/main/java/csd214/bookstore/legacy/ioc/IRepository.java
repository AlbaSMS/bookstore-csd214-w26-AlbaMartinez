package csd214.bookstore.legacy.ioc;
import csd214.bookstore.jpa.entities.ProductEntity;
import java.util.List;
public interface IRepository {
    ProductEntity save(ProductEntity entity);
    ProductEntity findById(Long id);
    List<ProductEntity> findAll();
    void delete(Long id);
    String getDataSourceType();
}
