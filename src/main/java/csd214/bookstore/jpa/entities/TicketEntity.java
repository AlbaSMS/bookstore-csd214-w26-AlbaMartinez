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

    @Override
    public void sellItem() {
        System.out.println("Selling Ticket: " + description + " for $" + price);
    }

    @Override
    public double getPrice() {
        return price;
    }

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
