package csd214.bookstore.legacy.ioc;

import jakarta.persistence.Persistence;
import java.util.Scanner;

public class IoCApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Expanded Inversion of Control Demo ===");
        System.out.println("Select your Repository Implementation:");
        System.out.println("1. In-Memory (Java List)");
        System.out.println("2. Generic JPA Repo -> Injected with H2 Config");
        System.out.println("3. Generic JPA Repo -> Injected with MySQL Config");
        System.out.println("4. H2Repository (Distinct Class)");
        System.out.println("5. MySqlRepository (Distinct Class)");
        System.out.print("Choice: ");

        int choice = 0;
        try { choice = Integer.parseInt(scanner.nextLine()); } catch(Exception e) {}

        IRepository repository;

        // --- WIRING PHASE ---
        switch (choice) {
            case 1:
                repository = new InMemoryRepository();
                break;
            case 2:
                // Generic approach: We decide the config here in Main
                repository = new JpaRepository(Persistence.createEntityManagerFactory("h2-pu"), "H2 (Generic)");
                break;
            case 3:
                // Generic approach: We decide the config here in Main
                repository = new JpaRepository(Persistence.createEntityManagerFactory("mysql-pu"), "MySQL (Generic)");
                break;
            case 4:
                // Specific approach: The class itself decides the config
                repository = new H2Repository();
                break;
            case 5:
                // Specific approach: The class itself decides the config
                repository = new MySqlRepository();
                break;
            default:
                System.out.println("Invalid choice. Defaulting to In-Memory.");
                repository = new InMemoryRepository();
        }

        // --- INJECTION PHASE ---
        // The Service doesn't care if it's Generic, Specific, H2, or MySQL.
        // It just wants an IRepository.
        BookstoreService service = new BookstoreService(repository);

        // --- EXECUTION PHASE ---
        service.addBasicInventory();
        service.listInventory();

        service.performSale(1L);
        service.listInventory();

        System.exit(0);
    }
}
