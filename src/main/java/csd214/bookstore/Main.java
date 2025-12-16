package csd214.bookstore;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bookstore Application Started (Repository Pattern)");
        // The App class now relies on the Repository for all data access
        new App().run();
    }
}