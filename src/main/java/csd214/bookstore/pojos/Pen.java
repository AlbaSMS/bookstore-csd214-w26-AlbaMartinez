package csd214.bookstore.pojos;

public class Pen extends Stationery {
    private String color;

    @Override
    public void initialize() {
        super.initialize();
        IO.println("Enter Color: ");
        this.color = getInput("Unknown Color");
    }

    @Override
    public void sellItem() {
        IO.println("Selling" + color + "Pen...");
    }

    @Override
    public double getPrice() {
        return 0;
    }
}
