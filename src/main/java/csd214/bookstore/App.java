package csd214.bookstore;

import csd214.bookstore.pojos.*;
import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * The Controller.
 * Handles all User Interface (System.in/out) and orchestrates data updates.
 * No UI logic exists in the POJOs anymore.
 */
public class App {
    private List<SaleableItem> items = new ArrayList<>();
    private CashTill cashTill = new CashTill();
    private Scanner input = new Scanner(System.in);
    // Date formatter for user input
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    public void run() {
        populate();
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

            choice = promptInt("Enter choice: ");

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
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ============================================================
    // VIEW HELPERS (Input Handling)
    // ============================================================

    private String prompt(String message) {
        System.out.println(message);
        return input.nextLine().trim();
    }

    private int promptInt(String message) {
        System.out.println(message);
        String raw = input.nextLine().trim();
        try {
            return raw.isEmpty() ? 0 : Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.");
            return 0;
        }
    }

    private double promptDouble(String message) {
        System.out.println(message);
        String raw = input.nextLine().trim();
        try {
            return raw.isEmpty() ? 0.0 : Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.0.");
            return 0.0;
        }
    }

    private boolean promptBool(String message) {
        System.out.println(message + " (true/false)");
        String raw = input.nextLine().trim();
        return Boolean.parseBoolean(raw);
    }

    private Date promptDate(String message) {
        System.out.println(message + " (Format: dd-MMM-yyyy)");
        String raw = input.nextLine().trim();
        if (raw.isEmpty()) return new Date();
        try {
            return dateFormatter.parse(raw);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using today.");
            return new Date();
        }
    }

    // ============================================================
    // CONTROLLER LOGIC (Add Item)
    // ============================================================

    public void addItem() {
        System.out.println("\nAdd an item\n");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. Add DiscMag");
        System.out.println("4. Add Ticket");
        System.out.println("99. Back");

        int choice = promptInt("Enter choice:");
        if (choice == 99) return;

        switch(choice) {
            case 1: addBook(); break;
            case 2: addMagazine(); break;
            case 3: addDiscMag(); break;
            case 4: addTicket(); break;
            default: System.out.println("Invalid selection.");
        }
    }

    private void addBook() {
        String title = prompt("Enter Title:");
        String author = prompt("Enter Author:");
        double price = promptDouble("Enter Price:");
        int copies = promptInt("Enter Copies:");

        // Constructor Injection (Pure Data)
        items.add(new Book(author, title, price, copies));
        System.out.println("Book added successfully.");
    }

    private void addMagazine() {
        String title = prompt("Enter Title:");
        double price = promptDouble("Enter Price:");
        int copies = promptInt("Enter Copies:");
        int orderQty = promptInt("Enter Order Qty:");
        Date issueDate = promptDate("Enter Current Issue Date:");

        items.add(new Magazine(orderQty, issueDate, title, price, copies));
        System.out.println("Magazine added successfully.");
    }

    private void addDiscMag() {
        String title = prompt("Enter Title:");
        double price = promptDouble("Enter Price:");
        int copies = promptInt("Enter Copies:");
        int orderQty = promptInt("Enter Order Qty:");
        Date issueDate = promptDate("Enter Current Issue Date:");
        boolean hasDisc = promptBool("Does it have a disc?");

        items.add(new DiscMag(hasDisc, orderQty, issueDate, title, price, copies));
        System.out.println("Disc Magazine added successfully.");
    }

    private void addTicket() {
        Ticket t = new Ticket();
        t.description = prompt("Enter Description:");
        t.price = promptDouble("Enter Price:");
        items.add(t);
        System.out.println("Ticket added successfully.");
    }

    // ============================================================
    // CONTROLLER LOGIC (Edit Item)
    // ============================================================

    public void editItem() {
        System.out.println("Select item index to edit (0 to " + (items.size() - 1) + "):");
        listAllWithIndex();

        int idx = promptInt("Index:");
        if (idx < 0 || idx >= items.size()) {
            System.out.println("Invalid index.");
            return;
        }

        SaleableItem item = items.get(idx);

        // We manually check types and update fields.
        // This replaces the polymorphic 'item.edit()' from step_00.

        // 1. Handle Publication Fields (Shared by Book, Magazine, DiscMag)
        if (item instanceof Publication) {
            Publication p = (Publication) item;

            String newTitle = prompt("Edit Title [" + p.getTitle() + "]:");
            if (!newTitle.isEmpty()) p.setTitle(newTitle);

            double newPrice = promptDouble("Edit Price [" + p.getPrice() + "] (0 to keep):");
            if (newPrice != 0.0) p.setPrice(newPrice);

            int newCopies = promptInt("Edit Copies [" + p.getCopies() + "] (0 to keep):");
            if (newCopies != 0) p.setCopies(newCopies);
        }

        // 2. Handle Specific Fields
        if (item instanceof Book) {
            Book b = (Book) item;
            String newAuth = prompt("Edit Author [" + b.getAuthor() + "]:");
            if (!newAuth.isEmpty()) b.setAuthor(newAuth);
        }
        else if (item instanceof DiscMag) {
            // Check DiscMag BEFORE Magazine because DiscMag extends Magazine
            DiscMag dm = (DiscMag) item;
            editMagazineFields(dm); // Helper for shared Magazine fields

            boolean newDisc = promptBool("Edit Has Disc [" + dm.isHasDisc() + "]:");
            dm.setHasDisc(newDisc);
        }
        else if (item instanceof Magazine) {
            Magazine m = (Magazine) item;
            editMagazineFields(m);
        }
        else if (item instanceof Ticket) {
            Ticket t = (Ticket) item;
            String newDesc = prompt("Edit Description [" + t.description + "]:");
            if (!newDesc.isEmpty()) t.description = newDesc;

            double newPrice = promptDouble("Edit Price [" + t.price + "] (0 to keep):");
            if (newPrice != 0.0) t.price = newPrice;
        }

        System.out.println("Item updated.");
    }

    private void editMagazineFields(Magazine m) {
        int newQty = promptInt("Edit Order Qty [" + m.getOrderQty() + "] (0 to keep):");
        if (newQty != 0) m.setOrderQty(newQty);

        // Date editing is complex in CLI, simplified here:
        String dateStr = prompt("Edit Issue Date [" + m.getCurrentIssue() + "] (Enter to keep):");
        if (!dateStr.isEmpty()) {
            try {
                m.setCurrentIssue(dateFormatter.parse(dateStr));
            } catch (ParseException e) {
                System.out.println("Invalid date, keeping old one.");
            }
        }
    }

    // ============================================================
    // STANDARD OPERATIONS
    // ============================================================

    public void deleteItem() {
        System.out.println("Select item index to delete:");
        listAllWithIndex();
        int idx = promptInt("Index:");
        if (idx >= 0 && idx < items.size()) {
            items.remove(idx);
            System.out.println("Item deleted.");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    public void sellItem() {
        System.out.println("Select item index to sell:");
        listAllWithIndex();
        int idx = promptInt("Index:");
        if (idx >= 0 && idx < items.size()) {
            SaleableItem item = items.get(idx);
            cashTill.sellItem(item);
        } else {
            System.out.println("Invalid selection.");
        }
        cashTill.showTotal();
    }

    public void listAny() {
        System.out.println("\nAll Items");
        System.out.println("-----------");
        System.out.println("1. All");
        System.out.println("2. Books");
        System.out.println("3. Magazines");
        System.out.println("4. DiscMags");
        System.out.println("5. Tickets");

        int choice = promptInt("Filter by:");

        Class<?> filter = null;
        switch(choice) {
            case 2: filter = Book.class; break;
            case 3: filter = Magazine.class; break;
            case 4: filter = DiscMag.class; break;
            case 5: filter = Ticket.class; break;
            default: filter = null; // All
        }

        for (SaleableItem i : items) {
            boolean show = false;
            if (filter == null) {
                show = true;
            } else {
                // Precise filtering
                if (filter == Magazine.class && i instanceof DiscMag) {
                    show = false; // Don't show DiscMags when asking for Magazines
                } else if (filter.isInstance(i)) {
                    show = true;
                }
            }

            if (show) {
                System.out.println(i.toString());
            }
        }
    }

    private void listAllWithIndex() {
        for(int i=0; i<items.size(); i++) {
            System.out.println(i + ". " + items.get(i));
        }
    }

    // ============================================================
    // DATA SEEDING
    // ============================================================

    public void populate() {
        System.out.println("Populating data with JavaFaker...");
        Faker faker = new Faker();

        for (int i = 0; i < 2; i++) {
            // Book
            Book b = new Book(
                    faker.book().author(),
                    faker.book().title(),
                    faker.number().randomDouble(2, 10, 50), // Price
                    faker.number().numberBetween(1, 20)     // Copies
            );
            items.add(b);

            // Magazine
            Magazine m = new Magazine(
                    faker.number().numberBetween(100, 500), // Order Qty
                    faker.date().past(30, TimeUnit.DAYS),   // Date
                    faker.book().title() + " Monthly",      // Title
                    faker.number().randomDouble(2, 5, 15),  // Price
                    faker.number().numberBetween(5, 50)     // Copies
            );
            items.add(m);

            // DiscMag
            DiscMag dm = new DiscMag(
                    faker.bool().bool(),                    // Has Disc
                    faker.number().numberBetween(50, 200),  // Order Qty
                    faker.date().past(60, TimeUnit.DAYS),   // Date
                    "Tech Disc: " + faker.app().name(),     // Title
                    faker.number().randomDouble(2, 10, 25), // Price
                    faker.number().numberBetween(5, 30)     // Copies
            );
            items.add(dm);

            // Ticket
            Ticket t = new Ticket();
            t.description = "Concert: " + faker.rockBand().name();
            t.price = faker.number().randomDouble(2, 50, 150);
            items.add(t);
        }
    }
}