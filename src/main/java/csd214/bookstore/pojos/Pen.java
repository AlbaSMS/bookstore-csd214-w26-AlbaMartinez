package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Pen extends Stationery {
    private String color;
    private double price;
    private String brand;

    public Pen() {
        super();
    }

    public Pen(String brand, String color, double price) {
        super();
        this.brand = brand;
        this.color = color;
        this.price = price;
    }

    @Override
    public void initialize(Scanner input) {
        IO.println("Enter Color: ");
        this.color = getInput(input,"Unknown Color");
        IO.println("Enter Price: ");
        this.price = getInput(input, 0.0);
        IO.println("Enter Brand: ");
        this.brand = getInput(input,"Unknown Brand");
    }

    @Override
    public void sellItem() {
        IO.println("Selling " + color + " " + brand + " Pen...");
    }

    @Override
    public double getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pen pen = (Pen) o;
        return Double.compare(price, pen.price) == 0 && Objects.equals(color, pen.color) && Objects.equals(brand, pen.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color, price, brand);
    }

    @Override
    public String toString() {
        return "Pen{" +
                "color='" + color + '\'' +
                ", price=" + price +
                ", brand='" + brand + '\'' +
                '}';
    }
}
