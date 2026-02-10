package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Laptop extends Electronics {
    private double screenSizeInches;
    private double price;

    public Laptop() {
    }

    public Laptop(String brand, double screenSizeInches, double warrantyMonths) {
        super(brand);
        this.screenSizeInches = screenSizeInches;
        // set the productId
        setProductId(UUID.randomUUID().toString());
    }

    public double getScreenSizeInches() {
        return screenSizeInches;
    }

    public void setScreenSizeInches(double screenSizeInches) {
        this.screenSizeInches = screenSizeInches;
    }

    @Override
    public void initialize(Scanner input) {
        super.initialize(input);
        IO.println("Enter screen size (inches): ");
        this.screenSizeInches = getInput(input, 15.6);
        IO.println("Enter warranty months: ");
        setWarrantyMonths(getInput(input, getWarrantyMonths()));
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input); // Title, Price, Copies
        IO.println("Edit screen size (inches): [" + this.screenSizeInches + "]:");
        this.screenSizeInches = getInput(input, this.screenSizeInches);
        IO.println("Edit warranty months: ");
        setWarrantyMonths(getInput(input, getWarrantyMonths()));
    }

    @Override
    public void sellItem() {
        IO.println("Selling Laptop (" + screenSizeInches + " inch screen, " + getWarrantyMonths() + " months warranty)");
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Laptop laptop = (Laptop) o;
        return Double.compare(screenSizeInches, laptop.screenSizeInches) == 0 && Double.compare(price, laptop.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenSizeInches, price);
    }

    @Override
    public String toString() {
        return "Laptop{" +
                "screenSizeInches=" + screenSizeInches +
                ", price=" + price +
                '}';
    }
}
