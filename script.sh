#!/bin/bash

ENTITY_DIR="src/main/java/csd214/bookstore/jpa/entities"

# 1. Update ProductEntity
# - Adds implements SaleableItem
# - Imports the interface
cat <<EOF > "$ENTITY_DIR/ProductEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;
import csd214.bookstore.pojos.SaleableItem;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class ProductEntity implements Serializable, SaleableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Abstract method from SaleableItem.
    // Children (Ticket, Publication) already have getPrice(), so that satisfies the interface.
    // We leave sellItem() for children to implement specific logic.

    @Override
    public String toString() {
        return "ProductEntity{id=" + id + "}";
    }
}
EOF

# 2. Update TicketEntity
# - Implements sellItem() (Simple print, infinite stock)
cat <<EOF > "$ENTITY_DIR/TicketEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {

    private String description;

    @Column(name = "ticket_price")
    private double price;

    public TicketEntity() {}

    public TicketEntity(String description, double price) {
        this.description = description;
        this.price = price;
    }

    // --- SaleableItem Implementation ---
    @Override
    public void sellItem() {
        System.out.println("Selling Ticket: " + description + " for $" + price);
    }

    @Override
    public double getPrice() {
        return price;
    }
    // -----------------------------------

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

# 3. Update PublicationEntity
# - Implements sellItem() (Decrements copies)
cat <<EOF > "$ENTITY_DIR/PublicationEntity.java"
package csd214.bookstore.jpa.entities;

import jakarta.persistence.*;

@Entity
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

    // --- SaleableItem Implementation ---
    @Override
    public void sellItem() {
        if (copies > 0) {
            copies--;
            System.out.println("Sold '" + title + "'. Remaining copies: " + copies);
        } else {
            System.out.println("Cannot sell '" + title + "'. Out of stock.");
        }
    }

    @Override
    public double getPrice() {
        return price;
    }
    // -----------------------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

# Note: BookEntity, MagazineEntity, and DiscMagEntity inherit from PublicationEntity.
# They automatically inherit the sellItem() logic (decrementing copies) defined above.
# We don't need to modify them unless they need specific selling behavior.

echo "Entities updated: ProductEntity now implements SaleableItem."