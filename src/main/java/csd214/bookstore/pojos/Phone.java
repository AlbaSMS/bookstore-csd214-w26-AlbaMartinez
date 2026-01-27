package csd214.bookstore.pojos;

public class Phone extends Electronics {
    private boolean supports5G;

    @Override
    public void initialize() {
        super.initialize();
        IO.println("Supports 5G? (true/false): ");
        supports5G = getInput(true);
        IO.println("Enter warranty months: ");
        setWarrantyMonths(getInput(getWarrantyMonths()));
    }

    @Override
    public void edit() {
        super.edit();
        IO.println("Supports 5G [" + supports5G + "]: ");
        supports5G = getInput(supports5G);
        IO.println("Warranty months [" + getWarrantyMonths() + "]: ");
        setWarrantyMonths(getInput(getWarrantyMonths()));
    }

    @Override
    public void sellItem() {
        IO.println("Selling Phone (" + (supports5G ? "5G enabled" : "No 5G") + ", " + getWarrantyMonths() + " months warranty)");
    }

    @Override
    public double getPrice() {
        return supports5G ? 999.99 : 799.99;
    }
}
