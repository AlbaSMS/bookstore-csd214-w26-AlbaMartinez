package csd214.bookstore.pojos;
import java.util.Objects;
public class Book extends Publication {
    private String author = "";
    public Book() {
        super();
    }
    public Book(String author, String title, double price, int copies) {
        super(title, price, copies);
        this.author = author;
    }
    // REMOVED: initialize(), edit()
    @Override
    public void sellItem() {
        System.out.println("Selling Book: " + getTitle() + " by " + author);
        setCopies(getCopies() - 1);
    }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    @Override
    public String toString() {
        return "Book{author='" + author + "', " + super.toString() + "}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return Objects.equals(author, book.author);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author);
    }
}
