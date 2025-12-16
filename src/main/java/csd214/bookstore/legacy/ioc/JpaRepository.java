package csd214.bookstore.legacy.ioc;
import csd214.bookstore.jpa.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class JpaRepository implements IRepository {
    private EntityManagerFactory emf;
    private EntityManager em;
    private String dbName;

    public JpaRepository(EntityManagerFactory emf, String dbName) {
        this.emf = emf;
        this.em = emf.createEntityManager();
        this.dbName = dbName;
    }

    @Override public ProductEntity save(ProductEntity entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (entity.getId() == null) em.persist(entity);
            else entity = em.merge(entity);
            tx.commit();
            return entity;
        } catch (Exception e) { if (tx.isActive()) tx.rollback(); throw e; }
    }
    @Override public ProductEntity findById(Long id) { return em.find(ProductEntity.class, id); }
    @Override public List<ProductEntity> findAll() {
        return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
    }
    @Override public void delete(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProductEntity p = em.find(ProductEntity.class, id);
            if(p != null) em.remove(p);
            tx.commit();
        } catch (Exception e) { if (tx.isActive()) tx.rollback(); }
    }
    @Override public String getDataSourceType() { return "JPA DATABASE: " + dbName; }
}
