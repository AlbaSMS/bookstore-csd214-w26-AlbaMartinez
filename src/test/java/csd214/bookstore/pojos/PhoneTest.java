package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhoneTest {
    @Test
    public void testSellItem() {
        Phone phone = new Phone("iPhone", true, 999.99,10);
        phone.sellItem();
        assertEquals(9, phone.getCopies(),"There should be 9 copies left");
    }

    @Test
    void testEquality() {
        Phone p1 = new Phone("iPhone", true, 999.99, 10);
        Phone p2 = new Phone("iPhone", true, 999.99, 10);
        Phone p3 = new Phone("Samsung", false, 799.99, 15);

        assertEquals(p1, p2, "Phone with same state should be equal");
        assertEquals(p1.hashCode(), p2.hashCode(), "HashCodes must match");
        assertNotEquals(p1, p3, "Different price phones should not be equal");
    }
}
