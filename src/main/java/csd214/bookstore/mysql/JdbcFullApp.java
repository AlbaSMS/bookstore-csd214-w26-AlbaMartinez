package csd214.bookstore.mysql;

import csd214.bookstore.pojos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcFullApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASSWORD = "itstudies12345";

    public static void main(String[] args) {
        try {
            // 1. Setup: Drop old data and create table
            createTable();

            // 2. CREATE
            System.out.println("\n=== INSERTING DATA ===");
            insertBook(new Book("George Orwell", "1984", 15.99, 10));
            insertMagazine(new Magazine(500, new java.util.Date(), "Time", 5.99, 50));
            insertDiscMag(new DiscMag(true, 200, new java.util.Date(), "PC Gamer", 12.99, 20));
            insertTicket(new Ticket());

            listAllItems();

            // 3. UPDATE
            System.out.println("\n=== UPDATING TICKET PRICE ===");
            updateTicketPrice("Concert Ticket", 150.00);
            listAllItems();

            // 4. DELETE
            System.out.println("\n=== DELETING MAGAZINE ===");
            deleteItemByTitle("Time");
            listAllItems();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createTable() throws SQLException {
        // --- CHANGED: Drop table first to ensure a clean slate ---
        String dropSql = "DROP TABLE IF EXISTS products";

        String createSql = "CREATE TABLE products (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "product_type VARCHAR(50), " +
                     "price DOUBLE, " +
                     "title VARCHAR(255), " +
                     "copies INT, " +
                     "author VARCHAR(255), " +
                     "order_qty INT, " +
                     "issue_date DATE, " +
                     "has_disc BOOLEAN, " +
                     "description VARCHAR(255)" +
                     ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute Drop
            stmt.execute(dropSql);
            System.out.println("Existing table 'products' dropped.");

            // Execute Create
            stmt.execute(createSql);
            System.out.println("New table 'products' created.");
        }
    }

    // --- INSERT METHODS ---

    private static void insertBook(Book b) throws SQLException {
        String sql = "INSERT INTO products (product_type, title, price, copies, author) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "BOOK");
            ps.setString(2, b.getTitle());
            ps.setDouble(3, b.getPrice());
            ps.setInt(4, b.getCopies());
            ps.setString(5, b.getAuthor());
            ps.executeUpdate();
            System.out.println("Inserted Book: " + b.getTitle());
        }
    }

    private static void insertMagazine(Magazine m) throws SQLException {
        String sql = "INSERT INTO products (product_type, title, price, copies, order_qty, issue_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "MAGAZINE");
            ps.setString(2, m.getTitle());
            ps.setDouble(3, m.getPrice());
            ps.setInt(4, m.getCopies());
            ps.setInt(5, m.getOrderQty());
            ps.setDate(6, new java.sql.Date(m.getCurrentIssue().getTime()));
            ps.executeUpdate();
            System.out.println("Inserted Magazine: " + m.getTitle());
        }
    }

    private static void insertDiscMag(DiscMag dm) throws SQLException {
        String sql = "INSERT INTO products (product_type, title, price, copies, order_qty, issue_date, has_disc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "DISCMAG");
            ps.setString(2, dm.getTitle());
            ps.setDouble(3, dm.getPrice());
            ps.setInt(4, dm.getCopies());
            ps.setInt(5, dm.getOrderQty());
            ps.setDate(6, new java.sql.Date(dm.getCurrentIssue().getTime()));
            ps.setBoolean(7, dm.isHasDisc());
            ps.executeUpdate();
            System.out.println("Inserted DiscMag: " + dm.getTitle());
        }
    }

    private static void insertTicket(Ticket t) throws SQLException {
        t.description = "Concert Ticket";
        t.price = 75.00;

        String sql = "INSERT INTO products (product_type, description, price) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "TICKET");
            ps.setString(2, t.description);
            ps.setDouble(3, t.price);
            ps.executeUpdate();
            System.out.println("Inserted Ticket: " + t.description);
        }
    }

    // --- READ METHOD ---

    private static void listAllItems() throws SQLException {
        String sql = "SELECT * FROM products";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Current Database Inventory ---");
            while (rs.next()) {
                String type = rs.getString("product_type");
                int id = rs.getInt("id");

                if ("BOOK".equals(type)) {
                    Book b = new Book();
                    b.setTitle(rs.getString("title"));
                    b.setPrice(rs.getDouble("price"));
                    b.setCopies(rs.getInt("copies"));
                    b.setAuthor(rs.getString("author"));
                    System.out.println("[ID " + id + "] " + b);
                }
                else if ("MAGAZINE".equals(type)) {
                    Magazine m = new Magazine();
                    m.setTitle(rs.getString("title"));
                    m.setPrice(rs.getDouble("price"));
                    m.setCopies(rs.getInt("copies"));
                    m.setOrderQty(rs.getInt("order_qty"));
                    m.setCurrentIssue(rs.getDate("issue_date"));
                    System.out.println("[ID " + id + "] " + m);
                }
                else if ("DISCMAG".equals(type)) {
                    DiscMag dm = new DiscMag();
                    dm.setTitle(rs.getString("title"));
                    dm.setPrice(rs.getDouble("price"));
                    dm.setCopies(rs.getInt("copies"));
                    dm.setOrderQty(rs.getInt("order_qty"));
                    dm.setCurrentIssue(rs.getDate("issue_date"));
                    dm.setHasDisc(rs.getBoolean("has_disc"));
                    System.out.println("[ID " + id + "] " + dm);
                }
                else if ("TICKET".equals(type)) {
                    Ticket t = new Ticket();
                    t.description = rs.getString("description");
                    t.price = rs.getDouble("price");
                    System.out.println("[ID " + id + "] " + t);
                }
            }
        }
    }

    // --- UPDATE METHOD ---

    private static void updateTicketPrice(String description, double newPrice) throws SQLException {
        String sql = "UPDATE products SET price = ? WHERE description = ? AND product_type = 'TICKET'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setString(2, description);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " ticket(s).");
        }
    }

    // --- DELETE METHOD ---

    private static void deleteItemByTitle(String title) throws SQLException {
        String sql = "DELETE FROM products WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            int rows = ps.executeUpdate();
            System.out.println("Deleted " + rows + " item(s) with title '" + title + "'.");
        }
    }
}
