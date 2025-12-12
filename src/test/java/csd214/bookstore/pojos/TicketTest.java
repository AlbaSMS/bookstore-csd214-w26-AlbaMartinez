package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testFields() {
        Ticket t = new Ticket();
        t.description = "Concert";
        t.price = 50.0;

        assertEquals("Concert", t.description);
        assertEquals(50.0, t.getPrice());
    }

    @Test
    void testSellItem() {
        // Tickets don't implement logic to reduce copies (they have no copies field)
        // But we check that calling sellItem() doesn't throw an exception
        Ticket t = new Ticket();
        t.description = "Event";
        t.price = 10.0;

        assertDoesNotThrow(() -> t.sellItem());
    }
}