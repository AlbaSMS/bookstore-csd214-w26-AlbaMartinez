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

    @GetMapping("/products")
    public List<ProductEntity> getExpensiveItems() {
        return repository.findAll();
    }
}
