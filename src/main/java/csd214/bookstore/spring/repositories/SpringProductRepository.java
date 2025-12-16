package csd214.bookstore.spring.repositories;

import csd214.bookstore.jpa.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringProductRepository extends JpaRepository<ProductEntity, Long> {
    // Standard CRUD methods (save, findAll, delete, findById) are provided automatically.

    // We removed the custom 'findByPriceGreaterThan' method.
    // This allows the app to start without modifying the ProductEntity hierarchy.
}