package csd214.bookstore.repositories;

import java.util.List;

public interface IRepository<T> {
    T save(T entity);
    T findById(Long id);
    List<T> findAll();
    void delete(Long id);
    void close(); // To close EntityManager resource
}
