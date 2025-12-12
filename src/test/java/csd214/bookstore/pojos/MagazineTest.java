package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class MagazineTest {

    @Test
    void testConstructorAndGetters() {
        Date now = new Date();
        Magazine mag = new Magazine(100, now, "Time", 5.99, 20);

        assertEquals("Time", mag.getTitle());
        assertEquals(5.99, mag.getPrice());
        assertEquals(20, mag.getCopies());
        assertEquals(100, mag.getOrderQty());
        assertEquals(now, mag.getCurrentIssue());
    }

    @Test
    void testSellItem() {
        Magazine mag = new Magazine(10, new Date(), "Vogue", 10.0, 5);
        mag.sellItem();
        assertEquals(4, mag.getCopies());
    }

    @Test
    void testSetters() {
        Magazine mag = new Magazine();
        Date date = new Date();

        mag.setOrderQty(500);
        mag.setCurrentIssue(date);

        assertEquals(500, mag.getOrderQty());
        assertEquals(date, mag.getCurrentIssue());
    }
}