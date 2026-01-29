package csd214.bookstore.pojos;

import java.util.Objects;

public class Book extends Publication {
    private String author = "";
    private String isbn = "";

    public Book() {
        super();
    }

    public Book(String author, String isbn) {
        this.author = author;
        this.isbn = isbn;
    }

    public Book(String author, String isbn, String title, double price, int copies) {
        super(title, price, copies);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public void initialize() {
        // 1. Initialize Parent (Title)
        super.initialize();

        // 2. Initialize Self (Author)
        System.out.println("Enter Author:");
        this.author = getInput("Unknown Author");

        System.out.println("Enter International Standard Book Number:");
        this.isbn = getInput("Unknown isbn");

        // 3. Initialize Parent (Copies/Price)
        super.initPriceCopies();
    }

    @Override
    public void edit() {
        // 1. Edit Parent fields (Title, Price, Copies)
        super.edit();

        // 2. Edit Self fields
        System.out.println("Edit Author [" + this.author + "]:");
        this.author = getInput(this.author);

        System.out.println("Edit Isbn [" + this.isbn + "]:");
        this.isbn = getInput(this.isbn);
    }

    @Override
    public void sellItem() {
        System.out.println("Selling Book: " + getTitle() + " by " + author);
        setCopies(getCopies() - 1);
    }

    public String getAuthor() {
        return author;
    }
    public String getIsbn() { return isbn; }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void getIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Book book = (Book) o;
        return Objects.equals(author, book.author) && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author, isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
