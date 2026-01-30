package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhoneTest {
    @Test
    public void testSellItem() {
        Phone phone = new Phone(10, true, 999.99);
        phone.sellItem();
        assertEquals(9, phone.getCopies(),"There should be 9 copies left");
    }
}
