package csd214.bookstore.legacy.ioc;
import csd214.bookstore.jpa.entities.ProductEntity;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements IRepository {
    private List<ProductEntity> db = new ArrayList<>();
    private Long idCounter = 1L;

    @Override public ProductEntity save(ProductEntity entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter++);
            db.add(entity);
        } else {
            delete(entity.getId());
            db.add(entity);
        }
        return entity;
    }
    @Override public ProductEntity findById(Long id) {
        return db.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }
    @Override public List<ProductEntity> findAll() { return new ArrayList<>(db); }
    @Override public void delete(Long id) { db.removeIf(p -> p.getId().equals(id)); }
    @Override public String getDataSourceType() { return "JAVA LIST (Volatile Memory)"; }
}
