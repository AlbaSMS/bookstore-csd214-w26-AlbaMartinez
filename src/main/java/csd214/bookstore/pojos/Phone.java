package csd214.bookstore.pojos;

public class Phone extends Electronics {
    private boolean supports5G;

    public boolean isSupports5G() {
        return supports5G;
    }

    public void setSupports5G(boolean supports5G) {
        this.supports5G = supports5G;
    }

    @Override
    public void initialize() {
        super.initialize();
        supports5G = true;
    }

    @Override
    public void edit() {
        super.edit();
        IO.println("Editing phone-specific fields...");
    }

    @Override
    public void sellItem() {
        IO.println("Selling phone...");
    }

    @Override
    public double getPrice() {
        if (supports5G) {
            return 999.99;
        }
        return 799.99;
    }
}
