package csd214.bookstore.pojos;

public class Laptop extends Electronics {
    private double screenSizeInches;

    @Override
    public void initialize() {
        super.initialize();
        IO.println("Enter screen size (inches): ");
        screenSizeInches = getInput(15.6);
        IO.println("Enter warranty months: ");
        setWarrantyMonths(getInput(getWarrantyMonths()));
    }

    @Override
    public void edit() {
        super.edit();
        IO.println("Edit screen size [" + screenSizeInches + "]: ");
        screenSizeInches = getInput(screenSizeInches);
        IO.println("Edit warranty months [" + getWarrantyMonths() + "]: ");
        setWarrantyMonths(getInput(getWarrantyMonths()));
    }

    @Override
    public void sellItem() {
        IO.println("Selling Laptop (" + screenSizeInches + " inch screen, " + getWarrantyMonths() + " months warranty)");
    }

    @Override
    public double getPrice() {
        return 1299.99;
    }
}
