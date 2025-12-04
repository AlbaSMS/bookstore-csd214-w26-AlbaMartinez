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
