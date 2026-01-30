package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Notebook extends Stationery {
    private int pageCount;
    public double price = 0;

    public Notebook() {
        super();
    }

    public Notebook(String brand, int pageCount) {
        super(brand);
        this.pageCount = pageCount;
    }

    @Override
    public void initialize(Scanner input) {
//        super.initialize(input);
        IO.println("Enter Page Count: ");
        this.pageCount = getInput(input, 0);
    }

    @Override
    public void sellItem() {
        IO.println("Selling" + getBrand() + "Notebook with " + pageCount + "pages...");
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Notebook notebook = (Notebook) o;
        return pageCount == notebook.pageCount && Double.compare(price, notebook.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pageCount, price);
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "pageCount=" + pageCount +
                ", price=" + price +
                '}';
    }
}
