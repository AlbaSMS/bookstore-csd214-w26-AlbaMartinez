package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Phone extends Electronics {
    private boolean supports5G;
    public double price;

    public Phone(int copies, boolean supports5G, double price) {
        this.copies = copies;
    }

    public Phone() {
    }

    @Override
    public void initialize(Scanner input) {
        super.initialize(input);
        IO.println("Supports 5G? (true/false): ");
        supports5G = getInput(input, true);
        IO.println("Enter warranty months: ");
        setWarrantyMonths(getInput(input, getWarrantyMonths()));

    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        IO.println("Supports 5G [" + supports5G + "]: ");
        supports5G = getInput(input, supports5G);
        price = supports5G ? 999.99 : 799.99;
        IO.println("Warranty months [" + getWarrantyMonths() + "]: ");
        setWarrantyMonths(getInput(input, getWarrantyMonths()));
    }

    @Override
    public void sellItem() {
        IO.println("Selling Phone (" + (supports5G ? "5G enabled" : "No 5G") + ", " + getWarrantyMonths() + " months warranty)");
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Phone phone = (Phone) o;
        return supports5G == phone.supports5G && Double.compare(price, phone.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), supports5G, price);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "supports5G=" + supports5G +
                ", price=" + price +
                '}';
    }
}
