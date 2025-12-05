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
