package csd214.bookstore.pojos;

import java.util.Objects;

public abstract class Stationary extends Product {
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Stationary() {
    }

    @Override
    public String toString() {
        return "Stationary{" +
                "brand='" + brand + '\'' +
                "}" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stationary that)) return false;
        return Objects.equals(getBrand(), that.getBrand());
    }

    @Override
    public void initialized() {
        // super.initialize();;
        IO.println("Enter brand: ");
        // this.brand = getInput("Generic");
        setBrand(getInput("Generic"));
    }

    @Override
    public void edit() {
        // 1. Edit Parent fields (Title, Price, Copies)
        // super.edit();
        // 2. Edit Self fields
        IO.println("Edit Author [" + this.brand + "]: ");
        this.brand = getInput(this.brand);
    }
}
