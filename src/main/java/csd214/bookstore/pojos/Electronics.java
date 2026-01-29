package csd214.bookstore.pojos;

import java.util.Scanner;

public abstract class Electronics extends Product {
    private int warrantyMonths;

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public void initialize(Scanner input) {
        warrantyMonths = 12; // Default
    }

    @Override
    public void edit(Scanner input) {
        IO.println("Editing electronic item...");
    }
}
