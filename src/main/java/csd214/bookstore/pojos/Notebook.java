package csd214.bookstore.pojos;

public class Notebook extends Stationery {
    private int pageCount;

    @Override
    public void initialize() {
        super.initialize();
        IO.println("Enter Page Count: ");
        this.pageCount = getInput(0);
    }

    @Override
    public void sellItem() {
        IO.println("Selling" + getBrand() + "Notebook with " + pageCount + "pages...");
    }

    @Override
    public double getPrice() {
        return 0;
    }
}
