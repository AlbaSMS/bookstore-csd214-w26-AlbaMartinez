package csd214.bookstore.legacy.ioc;

import jakarta.persistence.Persistence;

/**
 * A concrete Repository implementation specifically for H2.
 * It hardcodes the persistence unit name "h2-pu".
 */
public class H2Repository extends JpaRepository {

    public H2Repository() {
        // We call the parent constructor, injecting the H2-specific factory
        super(Persistence.createEntityManagerFactory("h2-pu"), "H2 (Distinct Class)");
    }

    // You could add H2-specific methods here if needed
}
