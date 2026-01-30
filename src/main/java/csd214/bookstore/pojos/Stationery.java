package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public abstract class Stationery extends Product {
    private String brand;

    public Stationery(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Stationery() {}

    @Override
    public String toString() {
        return "Stationery{" +
                "brand='" + brand + '\'' +
                "}" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stationery that)) return false;
        return Objects.equals(getBrand(), that.getBrand());
    }

    @Override
    public int hashCode() { return Objects.hashCode(getBrand()); }

    @Override
    public void initialize(Scanner input) {
        // super.initialize(input);;
        IO.println("Enter brand: ");
        // this.brand = getInput("Generic");
        setBrand(getInput(input, "Generic"));
    }

    @Override
    public void edit(Scanner input) {
        // 1. Edit Parent fields (Title, Price, Copies)
        // super.edit(input);
        // 2. Edit Self fields
        IO.println("Edit Brand [" + this.brand + "]: ");
        this.brand = getInput(input, this.brand);
    }
}