package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testConstructorAndGetters() {
        // Direct instantiation - no need to mock System.in
        Book book = new Book("J.K. Rowling", "Harry Potter", 29.99, 10);

        assertEquals("Harry Potter", book.getTitle());
        assertEquals("J.K. Rowling", book.getAuthor());
        assertEquals(29.99, book.getPrice());
        assertEquals(10, book.getCopies());
    }

    @Test
    void testNoArgConstructor() {
        // Essential for future ORM/Framework compatibility
        Book book = new Book();
        assertNotNull(book);
        assertEquals("", book.getTitle()); // Assuming default is empty string
        assertEquals(0.0, book.getPrice());
    }

    @Test
    void testSetters() {
        Book book = new Book();
        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setPrice(10.0);
        book.setCopies(5);

        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals(10.0, book.getPrice());
        assertEquals(5, book.getCopies());
    }

    @Test
    void testSellItem() {
        Book book = new Book("Author", "Title", 20.0, 5);

        book.sellItem();

        assertEquals(4, book.getCopies(), "Copies should decrease by 1 after sale");
    }

    @Test
    void testEqualsAndHashCode() {
        Book b1 = new Book("Author", "Title", 10.0, 1);
        Book b2 = new Book("Author", "Title", 10.0, 1);
        Book b3 = new Book("Other", "Title", 10.0, 1);

        assertEquals(b1, b2, "Books with same attributes should be equal");
        assertEquals(b1.hashCode(), b2.hashCode(), "HashCodes must match for equal objects");
        assertNotEquals(b1, b3, "Books with different authors should not be equal");
    }

    @Test
    void testToString() {
        Book book = new Book("Author", "Title", 10.0, 1);
        String str = book.toString();

        assertTrue(str.contains("Author"));
        assertTrue(str.contains("Title"));
        assertTrue(str.contains("Book"));
    }
}