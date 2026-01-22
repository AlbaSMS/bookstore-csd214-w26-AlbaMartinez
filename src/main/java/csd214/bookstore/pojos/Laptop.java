package csd214.bookstore.pojos;

public class Laptop extends Electronics {
    private double screenSizeInches;

    public double getScreenSizeInches() {
        return screenSizeInches;
    }

    public void setScreenSizeInches(double screenSizeInches) {
        this.screenSizeInches = screenSizeInches;
    }

    @Override
    public void initialize() {
        super.initialize();
        screenSizeInches = 15.6;
    }

    @Override
    public void edit() {
        super.edit();
        IO.println("Editing laptop-specific fields...");
    }

    @Override
    public void sellItem() {
        IO.println("Selling Laptop with a screen size of " + screenSizeInches + " inches");
    }

    @Override
    public double getPrice() {
        return 1299.99;
    }
}
