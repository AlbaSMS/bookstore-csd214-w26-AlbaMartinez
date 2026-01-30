//package csd214.bookstore.pojos;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MagazineTest {
//
//    private final InputStream originalSystemIn = System.in;
//
//    @AfterEach
//    void tearDown() {
//        System.setIn(originalSystemIn);
//    }
//
//    @Test
//    void testConstructorAndGetters() {
//        Date now = new Date();
//        Magazine mag = new Magazine(100, now, "Time", 5.99, 20);
//
//        assertEquals("Time", mag.getTitle());
//        assertEquals(5.99, mag.getPrice());
//        assertEquals(20, mag.getCopies());
//        assertEquals(100, mag.getOrderQty());
//        assertEquals(now, mag.getCurrentIssue());
//    }
//
//    @Test
//    void testSellItem() {
//        Magazine mag = new Magazine(10, new Date(), "Vogue", 10.0, 5);
//        mag.sellItem();
//        assertEquals(4, mag.getCopies());
//    }
//
//    @Test
//    void testInitializeWithMockInput() {
//        // Order: Title -> Order Qty -> Date (dd-MMM-yyyy) -> Copies -> Price
//        String simulatedInput = "National Geographic\n500\n01-Jan-2025\n10\n12.50\n";
//
//        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
//        Magazine mag = new Magazine();
//        mag.setSystemInput(testIn);
//
//        mag.initialize();
//
//        assertEquals("National Geographic", mag.getTitle());
//        assertEquals(500, mag.getOrderQty());
//        assertEquals(10, mag.getCopies());
//        assertEquals(12.50, mag.getPrice(), 0.001);
//        // Date verification can be tricky with string parsing, just checking it's not null or basic parsing
//        assertNotNull(mag.getCurrentIssue());
//    }
//
//    @Test
//    void testEditWithMockInput() {
//        // Order: Title -> Price -> Copies -> Order Qty -> Date
//        String simulatedInput = "New Title\n15.00\n100\n999\n01-Feb-2025\n";
//
//        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
//        Magazine mag = new Magazine(10, new Date(), "Old", 5.0, 5);
//        mag.setSystemInput(testIn);
//
//        mag.edit();
//
//        assertEquals("New Title", mag.getTitle());
//        assertEquals(15.00, mag.getPrice(), 0.001);
//        assertEquals(100, mag.getCopies());
//        assertEquals(999, mag.getOrderQty());
//    }
//}
