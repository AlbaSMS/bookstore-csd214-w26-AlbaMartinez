#!/bin/bash

TEST_DIR="src/test/java/csd214/bookstore/pojos"
mkdir -p "$TEST_DIR"

# ---------------------------------------------------------
# 1. MagazineTest
# ---------------------------------------------------------
cat <<EOF > "$TEST_DIR/MagazineTest.java"
package csd214.bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MagazineTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

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
    void testInitializeWithMockInput() {
        // Order: Title -> Order Qty -> Date (dd-MMM-yyyy) -> Copies -> Price
        String simulatedInput = "National Geographic\n500\n01-Jan-2025\n10\n12.50\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        Magazine mag = new Magazine();
        mag.setSystemInput(testIn);

        mag.initialize();

        assertEquals("National Geographic", mag.getTitle());
        assertEquals(500, mag.getOrderQty());
        assertEquals(10, mag.getCopies());
        assertEquals(12.50, mag.getPrice(), 0.001);
        // Date verification can be tricky with string parsing, just checking it's not null or basic parsing
        assertNotNull(mag.getCurrentIssue());
    }

    @Test
    void testEditWithMockInput() {
        // Order: Title -> Price -> Copies -> Order Qty -> Date
        String simulatedInput = "New Title\n15.00\n100\n999\n01-Feb-2025\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        Magazine mag = new Magazine(10, new Date(), "Old", 5.0, 5);
        mag.setSystemInput(testIn);

        mag.edit();

        assertEquals("New Title", mag.getTitle());
        assertEquals(15.00, mag.getPrice(), 0.001);
        assertEquals(100, mag.getCopies());
        assertEquals(999, mag.getOrderQty());
    }
}
EOF

# ---------------------------------------------------------
# 2. DiscMagTest
# ---------------------------------------------------------
cat <<EOF > "$TEST_DIR/DiscMagTest.java"
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
EOF

# ---------------------------------------------------------
# 3. TicketTest
# ---------------------------------------------------------
cat <<EOF > "$TEST_DIR/TicketTest.java"
package csd214.bookstore.pojos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
    }

    @Test
    void testFields() {
        Ticket t = new Ticket();
        t.description = "Concert";
        t.price = 50.0;

        assertEquals("Concert", t.description);
        assertEquals(50.0, t.getPrice());
    }

    @Test
    void testInitialize() {
        // Order: Description -> Price
        String simulatedInput = "Movie Night\n12.50\n";

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        Ticket t = new Ticket();
        t.setSystemInput(testIn);

        t.initialize();

        assertEquals("Movie Night", t.description);
        assertEquals(12.50, t.getPrice(), 0.001);
    }

    @Test
    void testEdit() {
        // Order: Description -> Price
        String simulatedInput = "Updated Event\n75.00\n";

        Ticket t = new Ticket();
        t.description = "Old Event";
        t.price = 20.0;

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        t.setSystemInput(testIn);

        t.edit();

        assertEquals("Updated Event", t.description);
        assertEquals(75.00, t.getPrice(), 0.001);
    }
}
EOF

# ---------------------------------------------------------
# 4. CashTillTest
# ---------------------------------------------------------
cat <<EOF > "$TEST_DIR/CashTillTest.java"
package csd214.bookstore.pojos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CashTillTest {

    @Test
    void testSellItemAddsToTotal() {
        CashTill till = new CashTill();
        assertEquals(0.0, till.getRunningTotal(), 0.001);

        // Create a dummy saleable item
        SaleableItem item = new SaleableItem() {
            @Override
            public void sellItem() { }

            @Override
            public double getPrice() { return 10.50; }
        };

        till.sellItem(item);
        assertEquals(10.50, till.getRunningTotal(), 0.001);

        till.sellItem(item);
        assertEquals(21.00, till.getRunningTotal(), 0.001);
    }
}
EOF

echo "All tests generated in $TEST_DIR"