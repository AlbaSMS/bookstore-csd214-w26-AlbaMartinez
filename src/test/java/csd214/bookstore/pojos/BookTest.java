package csd214.bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach
    void setUp() {
        // Capture output if needed for assertions
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Restore standard streams
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    void testConstructorAndGetters() {
        Book book = new Book("J.K. Rowling", "Harry Potter", 29.99, 10);

        assertEquals("Harry Potter", book.getTitle());
        assertEquals("J.K. Rowling", book.getAuthor());
        assertEquals(29.99, book.getPrice());
        assertEquals(10, book.getCopies());
    }

    @Test
    void testSellItem() {
        Book book = new Book("Author", "Title", 20.0, 5);

        book.sellItem();

        assertEquals(4, book.getCopies(), "Copies should decrease by 1 after sale");
    }

    @Test
    void testInitializeWithMockInput() {
        // Simulate User Input:
        // 1. Title (Publication.initialize)
        // 2. Author (Book.initialize)
        // 3. Copies (Publication.initPriceCopies)
        // 4. Price  (Publication.initPriceCopies)
        String simulatedInput = "The Hobbit\nJ.R.R. Tolkien\n15\n19.95\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());

        Book book = new Book();
        // Inject the mock stream
        book.setSystemInput(testIn);

        book.initialize();

        assertEquals("The Hobbit", book.getTitle());
        assertEquals("J.R.R. Tolkien", book.getAuthor());
        assertEquals(15, book.getCopies());
        assertEquals(19.95, book.getPrice(), 0.001);
    }

    @Test
    void testEditWithMockInput() {
        // Start with existing data
        Book book = new Book("Old Author", "Old Title", 10.0, 5);

        // Simulate User Input for edit():
        // 1. Title (Publication.edit) -> "New Title"
        // 2. Price (Publication.edit) -> "50.0"
        // 3. Copies (Publication.edit) -> "100"
        // 4. Author (Book.edit)        -> "New Author"
        String simulatedInput = "New Title\n50.0\n100\nNew Author\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        book.setSystemInput(testIn);

        book.edit();

        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals(50.0, book.getPrice(), 0.001);
        assertEquals(100, book.getCopies());
    }

    @Test
    void testEditWithEmptyInputPreservesValues() {
        // If user hits 'Enter' (empty string), values should remain unchanged.
        Book book = new Book("Old Author", "Old Title", 10.0, 5);

        // Simulate empty inputs (newlines)
        String simulatedInput = "\n\n\n\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        book.setSystemInput(testIn);

        book.edit();

        assertEquals("Old Title", book.getTitle());
        assertEquals("Old Author", book.getAuthor());
        assertEquals(10.0, book.getPrice());
        assertEquals(5, book.getCopies());
    }
}
