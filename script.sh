#!/bin/bash

# Define directories
ENTITY_DIR="src/main/java/csd214/bookstore/jpa/entities"
APP_DIR="src/main/java/csd214/bookstore/jpa"

# Create directories
mkdir -p "$ENTITY_DIR"
mkdir -p "$APP_DIR"

echo "Generating JPA Entities..."

# ---------------------------------------------------------
# 1. Base Entity: ProductEntity
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/ProductEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductEntity{id=" + id + "}";
    }
}
EOF

# ---------------------------------------------------------
# 2. TicketEntity
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/TicketEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {

    private String description;

    // Mapping to a specific column to avoid conflict with Publication's price
    @Column(name = "ticket_price")
    private double price;

    public TicketEntity() {}

    public TicketEntity(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + getId() +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
EOF

# ---------------------------------------------------------
# 3. PublicationEntity (Abstract)
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/PublicationEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;

@Entity
// We don't strictly need a DiscriminatorValue if we never instantiate PublicationEntity directly,
// but Hibernate might require it for the hierarchy.
public abstract class PublicationEntity extends ProductEntity {

    private String title;

    @Column(name = "pub_price")
    private double price;

    private int copies;

    public PublicationEntity() {}

    public PublicationEntity(String title, double price, int copies) {
        this.title = title;
        this.price = price;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return super.toString() + " PublicationEntity{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", copies=" + copies +
                '}';
    }
}
EOF

# ---------------------------------------------------------
# 4. BookEntity
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/BookEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class BookEntity extends PublicationEntity {

    private String author;

    public BookEntity() {}

    public BookEntity(String title, double price, int copies, String author) {
        super(title, price, copies);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + author + '\'' +
                ", price=" + getPrice() +
                ", copies=" + getCopies() +
                '}';
    }
}
EOF

# ---------------------------------------------------------
# 5. MagazineEntity
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/MagazineEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("MAGAZINE")
public class MagazineEntity extends PublicationEntity {

    private int orderQty;

    @Temporal(TemporalType.DATE)
    private Date currentIssue;

    public MagazineEntity() {}

    public MagazineEntity(String title, double price, int copies, int orderQty, Date currentIssue) {
        super(title, price, copies);
        this.orderQty = orderQty;
        this.currentIssue = currentIssue;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public Date getCurrentIssue() {
        return currentIssue;
    }

    public void setCurrentIssue(Date currentIssue) {
        this.currentIssue = currentIssue;
    }

    @Override
    public String toString() {
        return "MagazineEntity{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", issue=" + currentIssue +
                '}';
    }
}
EOF

# ---------------------------------------------------------
# 6. DiscMagEntity
# ---------------------------------------------------------
cat <<EOF > "$ENTITY_DIR/DiscMagEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("DISCMAG")
public class DiscMagEntity extends MagazineEntity {

    private boolean hasDisc;

    public DiscMagEntity() {}

    public DiscMagEntity(String title, double price, int copies, int orderQty, Date currentIssue, boolean hasDisc) {
        super(title, price, copies, orderQty, currentIssue);
        this.hasDisc = hasDisc;
    }

    public boolean isHasDisc() {
        return hasDisc;
    }

    public void setHasDisc(boolean hasDisc) {
        this.hasDisc = hasDisc;
    }

    @Override
    public String toString() {
        return "DiscMagEntity{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", hasDisc=" + hasDisc +
                '}';
    }
}
EOF

# ---------------------------------------------------------
# 7. JpaBookstoreApp (Demo Application)
# ---------------------------------------------------------
cat <<EOF > "$APP_DIR/JpaBookstoreApp.java"
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
EOF

echo "JPA Entities and Demo App created successfully."