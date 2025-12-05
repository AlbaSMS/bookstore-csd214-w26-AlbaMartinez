package csd214.bookstore.ioc;

import jakarta.persistence.Persistence;

/**
 * A concrete Repository implementation specifically for MySQL.
 * It hardcodes the persistence unit name "mysql-pu".
 */
public class MySqlRepository extends JpaRepository {

    public MySqlRepository() {
        // We call the parent constructor, injecting the MySQL-specific factory
        super(Persistence.createEntityManagerFactory("mysql-pu"), "MySQL (Distinct Class)");
    }

    // You could add MySQL-specific optimizations here if needed
}
