//package csd214.bookstore.pojos;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TicketTest {
//
//    private final InputStream originalSystemIn = System.in;
//
//    @AfterEach
//    void tearDown() {
//        System.setIn(originalSystemIn);
//    }
//
//    @Test
//    void testFields() {
//        Ticket t = new Ticket();
//        t.description = "Concert";
//        t.price = 50.0;
//
//        assertEquals("Concert", t.description);
//        assertEquals(50.0, t.getPrice());
//    }
//
//    @Test
//    void testInitialize() {
//        // Order: Description -> Price
//        String simulatedInput = "Movie Night\n12.50\n";
//
//        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
//        Ticket t = new Ticket();
//        t.setSystemInput(testIn);
//
//        t.initialize();
//
//        assertEquals("Movie Night", t.description);
//        assertEquals(12.50, t.getPrice(), 0.001);
//    }
//
//    @Test
//    void testEdit() {
//        // Order: Description -> Price
//        String simulatedInput = "Updated Event\n75.00\n";
//
//        Ticket t = new Ticket();
//        t.description = "Old Event";
//        t.price = 20.0;
//
//        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
//        t.setSystemInput(testIn);
//
//        t.edit();
//
//        assertEquals("Updated Event", t.description);
//        assertEquals(75.00, t.getPrice(), 0.001);
//    }
//}
