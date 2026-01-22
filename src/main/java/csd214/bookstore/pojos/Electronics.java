package csd214.bookstore.pojos;

public abstract class Electronics extends Product {
    private int warrantyMonths;

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public void initialize() {
        warrantyMonths = 12; // Default
    }

    @Override
    public void edit() {
        IO.println("Editing electronic item...");
    }
}
