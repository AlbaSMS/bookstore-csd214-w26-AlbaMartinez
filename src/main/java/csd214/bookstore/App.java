package csd214.bookstore;
import csd214.bookstore.jpa.entities.*;
import csd214.bookstore.pojos.*;
import com.github.javafaker.Faker;
import jakarta.persistence.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
/**
 * Controller class that bridges the User Interface (Console/POJOs)
 * with the Persistence Layer (JPA Entities).
 */
public class App {
    // We no longer use a List<SaleableItem>. The DB is our storage.
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);
    // JPA Components
    private EntityManagerFactory emf;
    private EntityManager em;
    public App() {
        // Initialize JPA connection based on persistence.xml configuration
        this.emf = Persistence.createEntityManagerFactory("bookstore-pu");
        this.em = emf.createEntityManager();
    }
    public void run() {
        // Pre-fill DB if empty
        if (getDBCount() == 0) {
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
                    System.out.println("Closing Database Connection...");
                    em.close();
                    emf.close();
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
        System.out.println("\nAll Items (Fetched from Database)");
        System.out.println("---------------------------------");
        // 1. Query the Database for all Products
        // Note: JPA handles the polymorphism. It returns BookEntity, TicketEntity, etc.
        List<ProductEntity> dbList = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        if (dbList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        // 2. Display
        for (int i = 0; i < dbList.size(); i++) {
            ProductEntity entity = dbList.get(i);
            // Convert to POJO to use the POJO's toString() format
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
        // 1. UI Interaction (User fills the POJO using the Console)
        // This reuses the Editable interface logic from step_00
        if (pojo instanceof Editable) {
            ((Editable) pojo).initialize();
        }
        // 2. Map POJO -> Entity
        ProductEntity entity = mapToEntity(pojo);
        // 3. Persist to DB
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            System.out.println("Saved to Database: " + entity.getClass().getSimpleName() + " [ID=" + entity.getId() + "]");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
    // ==========================================
    // CRUD: UPDATE
    // ==========================================
    public void editItem() {
        // 1. Fetch DB list to show indices
        List<ProductEntity> dbList = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();

        System.out.println("Select item index to edit:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i))); // Quick display
        }
        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                // 2. Get the MANAGED entity (It is currently 'attached' to the Persistence Context)
                ProductEntity entity = dbList.get(idx);
                // 3. Create a detached POJO for the user to edit
                SaleableItem pojo = mapToPojo(entity);
                if (pojo instanceof Editable) {
                    // 4. UI Interaction (User types new values)
                    ((Editable) pojo).edit();
                    // 5. Copy values back from POJO to Entity
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    updateEntityFromPojo(entity, pojo);

                    // 6. Commit
                    // Note: We don't need to call em.persist().
                    // Hibernate 'Dirty Checking' detects the changes to 'entity' and updates the DB.
                    tx.commit();
                    System.out.println("Database updated.");
                } else {
                    System.out.println("Item is not editable.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection or database error.");
            e.printStackTrace();
        }
    }
    // ==========================================
    // CRUD: DELETE
    // ==========================================
    public void deleteItem() {
        List<ProductEntity> dbList = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        System.out.println("Select item index to delete:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i)));
        }
        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                ProductEntity entity = dbList.get(idx);
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                em.remove(entity); // Marks the entity for deletion
                tx.commit();       // Executes DELETE SQL
                System.out.println("Item deleted from Database.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting item.");
        }
    }
    // ==========================================
    // LOGIC: SALES
    // ==========================================
    public void sellItem() {
        List<ProductEntity> dbList = em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        System.out.println("Select item index to sell:");
        for (int i = 0; i < dbList.size(); i++) {
            System.out.println(i + ". " + mapToPojo(dbList.get(i)));
        }
        try {
            int idx = Integer.parseInt(input.nextLine().trim());
            if (idx >= 0 && idx < dbList.size()) {
                ProductEntity entity = dbList.get(idx);

                // Convert to POJO to check Price/Logic (and print the 'Selling...' message)
                SaleableItem pojo = mapToPojo(entity);

                // Execute Business Logic (Print message, decrement POJO copy count)
                pojo.sellItem();
                cashTill.sellItem(pojo); // Update Till total
                // Persistence: If stock changed (Publication), we must update DB
                if (entity instanceof PublicationEntity && pojo instanceof Publication) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    // Sync the new copy count from POJO back to Entity
                    ((PublicationEntity) entity).setCopies(((Publication) pojo).getCopies());
                    tx.commit();
                    System.out.println("Inventory count updated in Database.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing sale.");
        }
    }
    // ==========================================
    // MAPPERS: POJO <-> ENTITY
    // ==========================================
    /**
     * Converts a DB Entity into a UI POJO.
     * This effectively treats our Editable classes as DTOs.
     */
    private SaleableItem mapToPojo(ProductEntity entity) {
        if (entity instanceof BookEntity) {
            BookEntity e = (BookEntity) entity;
            // Using loaded constructor to populate fields
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
    /**
     * Converts a UI POJO into a new DB Entity.
     */
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
    /**
     * Updates an existing DB Entity with values from a UI POJO.
     * Used during the 'Edit' workflow.
     */
    private void updateEntityFromPojo(ProductEntity entity, SaleableItem pojo) {
        // 1. Shared Publication Fields
        if (entity instanceof PublicationEntity && pojo instanceof Publication) {
            PublicationEntity pe = (PublicationEntity) entity;
            Publication pp = (Publication) pojo;
            pe.setTitle(pp.getTitle());
            pe.setPrice(pp.getPrice());
            pe.setCopies(pp.getCopies());
        }
        // 2. Specific Fields
        if (entity instanceof BookEntity && pojo instanceof Book) {
            ((BookEntity) entity).setAuthor(((Book) pojo).getAuthor());
        }
        else if (entity instanceof MagazineEntity && pojo instanceof Magazine) {
            MagazineEntity me = (MagazineEntity) entity;
            Magazine mp = (Magazine) pojo;
            me.setOrderQty(mp.getOrderQty());
            me.setCurrentIssue(mp.getCurrentIssue());

            // DiscMag inherits Magazine, so handle the specific Disc field here
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
    private long getDBCount() {
        return em.createQuery("SELECT COUNT(p) FROM ProductEntity p", Long.class).getSingleResult();
    }
    public void populate() {
        System.out.println("Seeding Database with JavaFaker...");
        Faker faker = new Faker();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        for (int i = 0; i < 2; i++) {
            // Book
            BookEntity b = new BookEntity(
                    faker.book().title(),
                    faker.number().randomDouble(2, 10, 50),
                    faker.number().numberBetween(1, 20),
                    faker.book().author()
            );
            em.persist(b);
            // Magazine
            MagazineEntity m = new MagazineEntity(
                    faker.book().title() + " Monthly",
                    faker.number().randomDouble(2, 5, 15),
                    faker.number().numberBetween(5, 50),
                    faker.number().numberBetween(100, 500),
                    faker.date().past(30, TimeUnit.DAYS)
            );
            em.persist(m);
            // DiscMag
            DiscMagEntity dm = new DiscMagEntity(
                    "Tech: " + faker.app().name(),
                    faker.number().randomDouble(2, 10, 25),
                    faker.number().numberBetween(5, 30),
                    faker.number().numberBetween(50, 200),
                    faker.date().past(60, TimeUnit.DAYS),
                    faker.bool().bool()
            );
            em.persist(dm);
            // Ticket
            TicketEntity t = new TicketEntity(
                    "Concert: " + faker.rockBand().name(),
                    faker.number().randomDouble(2, 50, 150)
            );
            em.persist(t);
        }
        tx.commit();
        System.out.println("Database Seeded.");

    }

}

