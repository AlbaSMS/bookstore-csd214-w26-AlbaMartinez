package csd214.bookstore.pojos;

import java.io.Serializable;

public class GiftWrap implements SaleableItem {
    public void sellItem() {
        IO.println("Gift Wrapping service sold!");
    }
    public double getPrice() {
        return 2.00;
    }
}