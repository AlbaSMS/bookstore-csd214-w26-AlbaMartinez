package csd214.bookstore.legacy;

import csd214.bookstore.legacy.ioc.IRepository;
import csd214.bookstore.jpa.entities.*;
import csd214.bookstore.pojos.*;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * The Controller / UI Layer.
 *
 * CHANGE LOG:
 * - Removed 'new ProductRepository()'
 * - Added Constructor Injection for IRepository
 * - This class is now 100% decoupled from the database implementation.
 */
public class LegacyApp {

    // Dependency: Interface only. No concrete class.
    private IRepository repository;

    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);

    // Constructor Injection
    public LegacyApp(IRepository repository) {
        this.repository = repository;
    }

    public void run() {
        System.out.println("App running with: " + repository.getDataSourceType());

        // Pre-fill DB if empty
        if (repository.findAll().isEmpty()) {
            populate();
        }

        int choice = 0;
        while (choice != 99) {
            System.out.println("\n***********************");
            System.out.println(" 1. Add Items");
            System.out.println(" 2. Edit Items");
            System.out.println(" 3. Delete Items");
            System.out.println(" 4. Sell item(s)");
            System.out.println(" 5. List items");
            System.out.println("99. Quit");
            System.out.println("***********************");
            System.out.print("Enter choice: \n");

            try {
                String line = input.nextLine();
                if (line.trim().isEmpty()) continue;
                choice = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                choice = 0;
            }

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    editItem();
                    break;
                case 3:
                    deleteItem();
                    break;
                case 4:
                    sellItem();
                    break;
                case 5:
                    listAny();
                    break;
                case 99:
                    System.out.println("Exiting...");
                    // No need to close() explicit resources here usually,
                    // but if the repo has a close method, we might call it.
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ==========================================
    // CRUD: READ
    // ==========================================

    public void listAny() {
        System.out.println("\nAll Items");
        System.out.println("----------------------------------");

        List<ProductEntity> dbList = repository.findAll();

        if (dbList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (int i = 0; i < dbList.size(); i++) {
            ProductEntity entity = dbList.get(i);
            SaleableItem pojo = mapToPojo(entity);
            System.out.println(i + ". " + pojo.toString());
        }
    }

    // ==========================================
    // CRUD: CREATE
    // ==========================================

    public void addItem() {
        System.out.println("\nAdd an item");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Add DiscMag");
        System.out.println("4. Add Ticket");
        System.out.println("99. Back");

        int choice = 0;
        try {
            String line = input.nextLine();
            choice = Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return;
        }

        if (choice == 99) return;

        SaleableItem pojo = null;
        switch (choice) {
            case 1: pojo = new Book(); break;
            case 2: pojo = new Magazine(); break;
            case 3: pojo = new DiscMag(); break;
            case 4: pojo = new Ticket(); break;
            default: System.out.println("Invalid selection."); return;
        }

        if (pojo instanceof Editable) {
            ((Editable) pojo).initialize();
        }

        ProductEntity entity = mapToEntity(pojo);

        try {
            repository.save(entity);
            System.out.println("Saved: " + entity.getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("Failed to save item: " + e.getMessage());
        }
    }

    // ==========================================
    // CRUD: UPDATE
    // ==========================================

    public void editItem() {
        List<ProductEntity> dbList = repository.findAll();

        System.out.println("Select item index to edit:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i)));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                ProductEntity entity = dbList.get(idx);
                SaleableItem pojo = mapToPojo(entity);

                if (pojo instanceof Editable) {
                    ((Editable) pojo).edit();
                    updateEntityFromPojo(entity, pojo);

                    repository.save(entity);
                    System.out.println("Item updated.");
                } else {
                    System.out.println("Item is not editable.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection or error.");
            e.printStackTrace();
        }
    }

    // ==========================================
    // CRUD: DELETE
    // ==========================================

    public void deleteItem() {
        List<ProductEntity> dbList = repository.findAll();

        System.out.println("Select item index to delete:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i)));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                ProductEntity entity = dbList.get(idx);
                repository.delete(entity.getId());
                System.out.println("Item deleted.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting item.");
        }
    }

    // ==========================================
    // LOGIC: SALES
    // ==========================================

    public void sellItem() {
        List<ProductEntity> dbList = repository.findAll();

        System.out.println("Select item index to sell:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i)));
        }

        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                ProductEntity entity = dbList.get(idx);
                SaleableItem pojo = mapToPojo(entity);

                pojo.sellItem();
                cashTill.sellItem(pojo);

                if (entity instanceof PublicationEntity && pojo instanceof Publication) {
                    ((PublicationEntity) entity).setCopies(((Publication) pojo).getCopies());
                    repository.save(entity);
                    System.out.println("Inventory count updated.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing sale.");
        }
    }

    // ==========================================
    // MAPPERS
    // ==========================================

    private SaleableItem mapToPojo(ProductEntity entity) {
        if (entity instanceof BookEntity) {
            BookEntity e = (BookEntity) entity;
            return new Book(e.getAuthor(), e.getTitle(), e.getPrice(), e.getCopies());
        }
        else if (entity instanceof DiscMagEntity) {
            DiscMagEntity e = (DiscMagEntity) entity;
            return new DiscMag(e.isHasDisc(), e.getOrderQty(), e.getCurrentIssue(), e.getTitle(), e.getPrice(), e.getCopies());
        }
        else if (entity instanceof MagazineEntity) {
            MagazineEntity e = (MagazineEntity) entity;
            return new Magazine(e.getOrderQty(), e.getCurrentIssue(), e.getTitle(), e.getPrice(), e.getCopies());
        }
        else if (entity instanceof TicketEntity) {
            TicketEntity e = (TicketEntity) entity;
            Ticket p = new Ticket();
            p.description = e.getDescription();
            p.price = e.getPrice();
            return p;
        }
        throw new IllegalArgumentException("Unknown Entity Type: " + entity.getClass());
    }

    private ProductEntity mapToEntity(SaleableItem pojo) {
        if (pojo instanceof Book) {
            Book p = (Book) pojo;
            return new BookEntity(p.getTitle(), p.getPrice(), p.getCopies(), p.getAuthor());
        }
        else if (pojo instanceof DiscMag) {
            DiscMag p = (DiscMag) pojo;
            return new DiscMagEntity(p.getTitle(), p.getPrice(), p.getCopies(), p.getOrderQty(), p.getCurrentIssue(), p.isHasDisc());
        }
        else if (pojo instanceof Magazine) {
            Magazine p = (Magazine) pojo;
            return new MagazineEntity(p.getTitle(), p.getPrice(), p.getCopies(), p.getOrderQty(), p.getCurrentIssue());
        }
        else if (pojo instanceof Ticket) {
            Ticket p = (Ticket) pojo;
            return new TicketEntity(p.description, p.price);
        }
        throw new IllegalArgumentException("Unknown POJO Type: " + pojo.getClass());
    }

    private void updateEntityFromPojo(ProductEntity entity, SaleableItem pojo) {
        if (entity instanceof PublicationEntity && pojo instanceof Publication) {
            PublicationEntity pe = (PublicationEntity) entity;
            Publication pp = (Publication) pojo;
            pe.setTitle(pp.getTitle());
            pe.setPrice(pp.getPrice());
            pe.setCopies(pp.getCopies());
        }

        if (entity instanceof BookEntity && pojo instanceof Book) {
            ((BookEntity) entity).setAuthor(((Book) pojo).getAuthor());
        }
        else if (entity instanceof MagazineEntity && pojo instanceof Magazine) {
            MagazineEntity me = (MagazineEntity) entity;
            Magazine mp = (Magazine) pojo;
            me.setOrderQty(mp.getOrderQty());
            me.setCurrentIssue(mp.getCurrentIssue());

            if (entity instanceof DiscMagEntity && pojo instanceof DiscMag) {
                ((DiscMagEntity) me).setHasDisc(((DiscMag) mp).isHasDisc());
            }
        }
        else if (entity instanceof TicketEntity && pojo instanceof Ticket) {
            TicketEntity te = (TicketEntity) entity;
            Ticket tp = (Ticket) pojo;
            te.setDescription(tp.description);
            te.setPrice(tp.price);
        }
    }

    // ==========================================
    // DATA SEEDING
    // ==========================================

    public void populate() {
        System.out.println("Seeding Database with JavaFaker...");
        Faker faker = new Faker();

        for (int i = 0; i < 2; i++) {
            BookEntity b = new BookEntity(
                    faker.book().title(),
                    faker.number().randomDouble(2, 10, 50),
                    faker.number().numberBetween(1, 20),
                    faker.book().author()
            );
            repository.save(b);

            MagazineEntity m = new MagazineEntity(
                    faker.book().title() + " Monthly",
                    faker.number().randomDouble(2, 5, 15),
                    faker.number().numberBetween(5, 50),
                    faker.number().numberBetween(100, 500),
                    faker.date().past(30, TimeUnit.DAYS)
            );
            repository.save(m);

            DiscMagEntity dm = new DiscMagEntity(
                    "Tech: " + faker.app().name(),
                    faker.number().randomDouble(2, 10, 25),
                    faker.number().numberBetween(5, 30),
                    faker.number().numberBetween(50, 200),
                    faker.date().past(60, TimeUnit.DAYS),
                    faker.bool().bool()
            );
            repository.save(dm);

            TicketEntity t = new TicketEntity(
                    "Concert: " + faker.rockBand().name(),
                    faker.number().randomDouble(2, 50, 150)
            );
            repository.save(t);
        }
        System.out.println("Database Seeded.");
    }
}