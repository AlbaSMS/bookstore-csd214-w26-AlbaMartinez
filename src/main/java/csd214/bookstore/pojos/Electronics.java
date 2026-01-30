package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public abstract class Electronics extends Product {
    private int warrantyMonths = 12;
    private String brand;

    protected int copies;

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public Electronics(String brand) {
        this.brand = brand;
    }

    public Electronics() { }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public void sellItem() {
        setCopies(copies - 1);
        }
    }

    @Override
    public void initialize(Scanner input) {
        IO.println("Enter brand: ");
        setBrand(getInput(input, "Generic"));
    };

    @Override
    public void edit(Scanner input) {
        IO.println("Edit Brand [" + this.brand + "]: ");
        this.brand = getInput(input, this.brand);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Electronics that = (Electronics) o;
        return warrantyMonths == that.warrantyMonths && copies == that.copies && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warrantyMonths, brand, copies);
    }

    @Override
    public String toString() {
        return "Electronics{" +
                "warrantyMonths=" + warrantyMonths +
                ", brand='" + brand + '\'' +
                ", copies=" + copies +
                '}';
    }
}