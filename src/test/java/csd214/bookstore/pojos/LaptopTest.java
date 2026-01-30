package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LaptopTest {
    @Test
    void testConstructor() {
        Laptop l = new Laptop("Lenovo", 15.6, 12);
        assertEquals("Lenovo", l.getBrand());
        assertEquals(15.6, l.getScreenSizeInches());
        assertEquals(12, l.getWarrantyMonths());
    }
}
