package csd214.bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DiscMagTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void testConstructor() {
        Date now = new Date();
        DiscMag dm = new DiscMag(true, 50, now, "PC Gamer", 9.99, 15);

        assertTrue(dm.isHasDisc());
        assertEquals("PC Gamer", dm.getTitle());
    }

    @Test
    void testSellItem() {
        DiscMag dm = new DiscMag(true, 50, new Date(), "PC Gamer", 9.99, 15);
        dm.sellItem();
        assertEquals(14, dm.getCopies());
    }

    @Test
    void testInitialize() {
        // Order (Magazine): Title -> Order Qty -> Date -> Copies -> Price
        // Order (DiscMag):  Has Disc?
        String simulatedInput = "TechWorld\n200\n15-Mar-2025\n25\n19.99\ntrue\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        DiscMag dm = new DiscMag();
        dm.setSystemInput(testIn);

        dm.initialize();

        assertEquals("TechWorld", dm.getTitle());
        assertEquals(19.99, dm.getPrice(), 0.001);
        assertTrue(dm.isHasDisc());
    }

    @Test
    void testEdit() {
        // Order (Magazine): Title -> Price -> Copies -> Order Qty -> Date
        // Order (DiscMag):  Has Disc?
        String simulatedInput = "New Tech\n25.00\n50\n300\n20-Mar-2025\nfalse\n";

        DiscMag dm = new DiscMag(true, 100, new Date(), "Old", 10.0, 10);
        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        dm.setSystemInput(testIn);

        dm.edit();

        assertEquals("New Tech", dm.getTitle());
        assertEquals(25.00, dm.getPrice(), 0.001);
        assertFalse(dm.isHasDisc());
    }
}
