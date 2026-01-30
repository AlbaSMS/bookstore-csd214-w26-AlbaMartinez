package csd214.bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

public class Widget extends Product {
    private String widgetName;
    private double price;

    public Widget() { setWidgetName("Default Widget Name"); }

    public Widget(String name, double price) {
        // set the productId
        setProductId(UUID.randomUUID().toString());
        setPrice(price);
        setWidgetName(name);
    }

    public String getWidgetName() { return widgetName; }

    public void setWidgetName(String widgetName) { this.widgetName = widgetName; }

    @Override
    public void edit(Scanner input) {
        IO.println("Enter Widget name (<" + getWidgetName() + ">) ' ");
    }

    @Override
    public void initialize(Scanner input) {
        IO.println("Enter Widget name (<Default Widget Name>) : ");
        setWidgetName(getInput(input, "Default Widget Name"));
        IO.println("Enter Widget price (<0>) : ");
        setPrice(getInput(input, 0.0d));
    }

    @Override
    public void sellItem() {

    }

    @Override
    public double getPrice() { return 0; }
    public void setPrice(double price) {
        if(price < 0) {
            throw new IllegalArgumentException("Price can not be negative");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "Widget{" +
                "widgetName='" + widgetName +'\'' +
                ", price=" + price +
                "} " + super.toString();
    }
}
