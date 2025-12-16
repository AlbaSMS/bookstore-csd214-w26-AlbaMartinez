package csd214.bookstore;

import csd214.bookstore.ioc.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bookstore Application Started");
        System.out.println("=================================");
        System.out.println("Select Data Persistence Strategy:");
        System.out.println("1. In-Memory (Fast, Volatile)");
        System.out.println("2. H2 Database (SQL, Volatile, Good for Testing)");
        System.out.println("3. MySQL Database (SQL, Persistent, Production)");
        System.out.print("Choice: ");

        Scanner sc = new Scanner(System.in);
        int choice = 0;
        try {
            String input = sc.nextLine();
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            choice = 1; // Default
        }

        // --- WIRING PHASE (IoC Container Logic) ---
        // We create the specific dependency here, outside the App.
        IRepository repository;

        switch (choice) {
            case 2:
                System.out.println(">> Initializing H2 Database...");
                repository = new H2Repository();
                break;
            case 3:
                System.out.println(">> Connecting to MySQL...");
                repository = new MySqlRepository();
                break;
            case 1:
            default:
                System.out.println(">> Using In-Memory List...");
                repository = new InMemoryRepository();
                break;
        }

        // --- INJECTION PHASE ---
        // We inject the chosen repository into the App.
        // App doesn't know (or care) which one it got.
        App app = new App(repository);

        // Run the application
        app.run();
    }
}