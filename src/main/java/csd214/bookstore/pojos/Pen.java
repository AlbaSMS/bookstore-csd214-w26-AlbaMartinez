package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Pen extends Stationery {
    private String color;

    public Pen() {
    }

    public Pen(String color) {
        this.color = color;
    }

    public Pen(String brand, String color, double price) {
        super(brand);
        this.color = color;
    }

    @Override
    public void initialize(Scanner input) {
        super.initialize(input);
        IO.println("Enter Color: ");
        this.color = getInput(input,"Unknown Color");
    }

    @Override
    public void sellItem() {
        IO.println("Selling " + color + " " + getBrand() + " Pen...");
    }

    @Override
    public double getPrice() {
        return 0;
    }

    public String getColor() {
        return color;
    }

    public void setColor() {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pen pen = (Pen) o;
        return Objects.equals(color, pen.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color);
    }

    @Override
    public String toString() {
        return "Pen{" +
                "color='" + color + '\'' +
                '}';
    }
}
