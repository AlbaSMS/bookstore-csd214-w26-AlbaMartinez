package csd214.bookstore.legacy.ioc;
import csd214.bookstore.jpa.entities.*;
import java.util.Date;
import java.util.List;

public class BookstoreService {
    private IRepository repository;

    public BookstoreService(IRepository repository) {
        this.repository = repository;
    }

    public void addBasicInventory() {
        System.out.println("Service: Adding default inventory...");
        repository.save(new BookEntity("The IoC Principle", 40.00, 10, "Spring Guru"));
        repository.save(new TicketEntity("Coding Bootcamp", 500.00));
        repository.save(new MagazineEntity("JPA Daily", 5.99, 100, 50, new Date()));
    }

    public void listInventory() {
        System.out.println("\n--- INVENTORY REPORT (" + repository.getDataSourceType() + ") ---");
        List<ProductEntity> items = repository.findAll();
        if (items.isEmpty()) System.out.println("(Inventory is empty)");
        for (ProductEntity p : items) System.out.println(p);
    }

    public void performSale(Long id) {
        System.out.println("\nService: Attempting to sell item ID: " + id);
        ProductEntity item = repository.findById(id);
        if (item != null) {
            item.sellItem();
            repository.save(item);
        } else {
            System.out.println("Service Error: Item not found.");
        }
    }
}
