package csd214.bookstore.jpa.entities;
import jakarta.persistence.*;

@Entity @DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {
    private String description;
    @Column(name = "ticket_price") private double price;
    public TicketEntity() {}
    public TicketEntity(String d, double p) { this.description = d; this.price = p; }

    @Override public void sellItem() { System.out.println("Selling Ticket: " + description + " for $" + price); }
    @Override public double getPrice() { return price; }

    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public void setPrice(double p) { this.price = p; }
    @Override public String toString() { return "Ticket{desc='" + description + "', price=" + price + "}"; }
}
