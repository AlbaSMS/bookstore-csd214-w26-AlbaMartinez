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

    @Test
    void testEquality() {
        Laptop l1 = new Laptop("Lenovo", 15.6, 12);
        Laptop l2 = new Laptop("Lenovo", 15.6, 12);
        Laptop l3 = new Laptop("Mac", 20, 6);

        assertEquals(l1, l2, "Laptop with same state should be equal");
        assertEquals(l1.hashCode(), l2.hashCode(), "HashCodes must match");
        assertNotEquals(l1, l3, "Different laptops should not be equal");
    }
}
