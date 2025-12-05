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
