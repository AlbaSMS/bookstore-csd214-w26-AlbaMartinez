package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class DiscMagTest {

    @Test
    void testConstructor() {
        Date now = new Date();
        // Check that the boolean flag is correctly passed up and stored
        DiscMag dm = new DiscMag(true, 50, now, "PC Gamer", 9.99, 15);

        assertTrue(dm.isHasDisc());
        assertEquals("PC Gamer", dm.getTitle());
        assertEquals(15, dm.getCopies());
    }

    @Test
    void testSellItem() {
        DiscMag dm = new DiscMag(true, 50, new Date(), "PC Gamer", 9.99, 15);
        dm.sellItem();
        assertEquals(14, dm.getCopies());
    }

    @Test
    void testInheritance() {
        DiscMag dm = new DiscMag();
        // Verify it is a Magazine
        assertTrue(dm instanceof Magazine);
        // Verify it is a Publication
        assertTrue(dm instanceof Publication);
    }
}