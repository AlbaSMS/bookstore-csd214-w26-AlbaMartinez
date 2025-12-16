#!/bin/bash

# Base directories
BASE_PKG="src/main/java/csd214/bookstore"
LEGACY_DIR="$BASE_PKG/legacy"
SPRING_DIR="$BASE_PKG/spring"

# 1. Refactor: Move Old Code to Legacy
echo "Moving manual IoC code to legacy package..."
mkdir -p "$LEGACY_DIR"

# Move Main and App
mv "$BASE_PKG/Main.java" "$LEGACY_DIR/"
mv "$BASE_PKG/App.java" "$LEGACY_DIR/"

# Move IoC package
mv "$BASE_PKG/ioc" "$LEGACY_DIR/"

# 2. Update Package Declarations in Legacy files
# (This uses sed to prepend .legacy to the package lines)

# Fix Main.java
sed -i 's/package csd214.bookstore;/package csd214.bookstore.legacy;/' "$LEGACY_DIR/Main.java"
sed -i 's/import csd214.bookstore.ioc.*;/import csd214.bookstore.legacy.ioc.*;/' "$LEGACY_DIR/Main.java"
sed -i 's/App app/LegacyApp app/' "$LEGACY_DIR/Main.java"
sed -i 's/new App/new LegacyApp/' "$LEGACY_DIR/Main.java"

# Fix App.java (Renaming to LegacyApp to avoid confusion)
mv "$LEGACY_DIR/App.java" "$LEGACY_DIR/LegacyApp.java"
sed -i 's/package csd214.bookstore;/package csd214.bookstore.legacy;/' "$LEGACY_DIR/LegacyApp.java"
sed -i 's/public class App/public class LegacyApp/' "$LEGACY_DIR/LegacyApp.java"
sed -i 's/public App(/public LegacyApp(/' "$LEGACY_DIR/LegacyApp.java"
sed -i 's/import csd214.bookstore.ioc.IRepository;/import csd214.bookstore.legacy.ioc.IRepository;/' "$LEGACY_DIR/LegacyApp.java"

# Fix IoC files
find "$LEGACY_DIR/ioc" -name "*.java" -exec sed -i 's/package csd214.bookstore.ioc;/package csd214.bookstore.legacy.ioc;/' {} +

echo "Legacy refactor complete."

# 3. Generate Spring Boot Code
echo "Generating Spring Boot Architecture..."
mkdir -p "$SPRING_DIR"
mkdir -p "$SPRING_DIR/repositories"
mkdir -p "$SPRING_DIR/controllers"

# A. Application Entry Point
cat <<EOF > "$SPRING_DIR/BookstoreSpringApplication.java"
package csd214.bookstore.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// 1. Scan for Controllers/Services in this package
@ComponentScan(basePackages = "csd214.bookstore.spring")
// 2. Scan for JPA Entities in the shared domain package
@EntityScan(basePackages = "csd214.bookstore.jpa.entities")
// 3. Scan for Magic Interfaces
@EnableJpaRepositories(basePackages = "csd214.bookstore.spring.repositories")
public class BookstoreSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreSpringApplication.class, args);
        System.out.println("Spring Boot Bookstore is running!");
        System.out.println("Access data at: http://localhost:8080/products");
    }
}
EOF

# B. Magic Repository (Replaces ProductRepository.java)
cat <<EOF > "$SPRING_DIR/repositories/SpringProductRepository.java"
package csd214.bookstore.spring.repositories;

import csd214.bookstore.jpa.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MAGIC INTERFACE:
 * We do NOT write a class implementing this.
 * Spring creates the implementation at runtime using Bytecode generation.
 */
@Repository
public interface SpringProductRepository extends JpaRepository<ProductEntity, Long> {

    // We get save(), findAll(), findById(), delete() automatically.

    // Custom Queries via Method Naming Convention:
    // "SELECT * FROM products WHERE price > ?"
    List<ProductEntity> findByPriceGreaterThan(double price);
}
EOF

# C. Basic REST Controller (To verify it works)
cat <<EOF > "$SPRING_DIR/controllers/ProductController.java"
package csd214.bookstore.spring.controllers;

import csd214.bookstore.jpa.entities.ProductEntity;
import csd214.bookstore.spring.repositories.SpringProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    // DEPENDENCY INJECTION (Field injection for simplicity in demo)
    private final SpringProductRepository repository;

    public ProductController(SpringProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ProductEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping("/expensive")
    public List<ProductEntity> getExpensiveItems() {
        return repository.findByPriceGreaterThan(20.00);
    }
}
EOF

echo "Spring Boot setup complete."