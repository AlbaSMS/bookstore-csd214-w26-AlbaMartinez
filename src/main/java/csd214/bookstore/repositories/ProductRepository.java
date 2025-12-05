package csd214.bookstore.repositories;

import csd214.bookstore.jpa.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class ProductRepository implements IRepository<ProductEntity> {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ProductRepository() {
        this.emf = Persistence.createEntityManagerFactory("bookstore-pu");
        this.em = emf.createEntityManager();
    }

    @Override
    public ProductEntity save(ProductEntity entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                entity = em.merge(entity);
            }
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public ProductEntity findById(Long id) {
        return em.find(ProductEntity.class, id);
    }

    @Override
    public List<ProductEntity> findAll() {
        return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
                 .getResultList();
    }

    @Override
    public void delete(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ProductEntity entity = em.find(ProductEntity.class, id);
            if (entity != null) {
                em.remove(entity);
                System.out.println("Deleted entity with ID: " + id);
            } else {
                System.out.println("Entity with ID " + id + " not found.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    @Override
    public void close() {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }
}
