package csd214.bookstore.jpa;

import csd214.bookstore.jpa.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class JpaBookstoreApp {

    public static void main(String[] args) {
        System.out.println("Starting JPA Bookstore Demo...");

        // 1. Initialize EntityManagerFactory (matches persistence-unit name in persistence.xml)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bookstore-pu");
        EntityManager em = emf.createEntityManager();

        try {
            // --- CREATE ---
            System.out.println("\n=== Creating Entities ===");
            createData(em);

            // --- READ ---
            System.out.println("\n=== Reading All Products ===");
            listProducts(em);

            // --- UPDATE ---
            System.out.println("\n=== Updating a Book ===");
            updateBookPrice(em, "The JPA Handbook", 50.00);
            listProducts(em);

            // --- DELETE ---
            System.out.println("\n=== Deleting a Ticket ===");
            deleteTicket(em, "Concert Ticket");
            listProducts(em);

        } finally {
            em.close();
            emf.close();
            System.out.println("\nDemo Finished.");
        }
    }

    private static void createData(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Create Book
        BookEntity book = new BookEntity("The JPA Handbook", 29.99, 10, "Java Guru");
        em.persist(book);
        System.out.println("Persisted: " + book);

        // Create Magazine
        MagazineEntity mag = new MagazineEntity("Tech Monthly", 9.99, 100, 500, new Date());
        em.persist(mag);
        System.out.println("Persisted: " + mag);

        // Create DiscMag
        DiscMagEntity discMag = new DiscMagEntity("Gamer World", 14.99, 50, 200, new Date(), true);
        em.persist(discMag);
        System.out.println("Persisted: " + discMag);

        // Create Ticket
        TicketEntity ticket = new TicketEntity("Concert Ticket", 99.00);
        em.persist(ticket);
        System.out.println("Persisted: " + ticket);

        tx.commit();
    }

    private static void listProducts(EntityManager em) {
        // Polymorphic query: Selects from all subclasses of ProductEntity
        List<ProductEntity> products = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
                .getResultList();

        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            for (ProductEntity p : products) {
                System.out.println(p);
            }
        }
    }

    private static void updateBookPrice(EntityManager em, String title, double newPrice) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Using JPQL to find specific book
        try {
            BookEntity book = em.createQuery("SELECT b FROM BookEntity b WHERE b.title = :title", BookEntity.class)
                    .setParameter("title", title)
                    .getSingleResult();

            System.out.println("Found book: " + book.getTitle() + ". Old Price: " + book.getPrice());
            book.setPrice(newPrice);
            System.out.println("Updated Price to: " + newPrice);

        } catch (Exception e) {
            System.out.println("Book not found for update: " + title);
        }

        tx.commit();
    }

    private static void deleteTicket(EntityManager em, String description) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            TicketEntity ticket = em.createQuery("SELECT t FROM TicketEntity t WHERE t.description = :desc", TicketEntity.class)
                    .setParameter("desc", description)
                    .getSingleResult();

            em.remove(ticket);
            System.out.println("Deleted ticket: " + description);

        } catch (Exception e) {
            System.out.println("Ticket not found for deletion: " + description);
        }

        tx.commit();
    }
}
