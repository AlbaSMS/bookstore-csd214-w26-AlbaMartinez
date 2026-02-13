package csd214.bookstore.mysql;

import csd214.bookstore.pojos.Notebook;

import java.sql.*;

public class JdbcNotebookApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASS = "itstudies12345";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Create Table
            createTable(conn);
            // 2. Insert
            System.out.println("--- INSERTING ---");
            Notebook notebook = new Notebook("Labon", 200);
            insertNotebook(conn, notebook);
            // 3. Read
            System.out.println("--- READING ---");
            listNotebooks(conn);
            // 4. Update
            System.out.println("--- UPDATING ---");
            updateNotebookPrice(conn, "Labon", 25.50, 200);

            // 5. Delete
            System.out.println("--- DELETING ---");
            deleteNotebook(conn, "Labon", 200);
            listNotebooks(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS notebooks (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id VARCHAR(36), " +
                "brand VARCHAR(255), " +
                "page_count INT, " +
                "price DOUBLE)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'notebooks' ready.");
        }
    }
    private static void insertNotebook(Connection conn, Notebook n) throws SQLException {
        // SECURITY: Use ? to prevent SQL Injection
        String sql = "INSERT INTO notebooks (product_id, brand, page_count, price ) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, n.getProductId()); // UUID
            ps.setString(2, n.getBrand());
            ps.setInt(3, n.getPageCount());
            ps.setDouble(4, n.getPrice());
            ps.executeUpdate();
            System.out.println("Saved: " + n.toString());
        }
    }
    private static void listNotebooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM notebooks";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | UUID: %s | Brand: %s | Page Count: %d%n | Price: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("product_id"),
                        rs.getString("brand"),
                        rs.getInt("page_count"),
                        rs.getDouble("price"));
            }
        }
    }
    private static void updateNotebookPrice(Connection conn, String brand, double newPrice, int pageCount) throws SQLException {
        String sql = "UPDATE notebooks SET price = ? WHERE brand = ? AND pageCount = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setString(2, brand);
            ps.setInt(3, pageCount);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " notebook(s).");
        }
    }

    private static void deleteNotebook(Connection conn, String brand, int pageCount) throws SQLException {
        String sql = "DELETE FROM notebooks WHERE brand = ? AND pageCount = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setInt(2, pageCount);
            ps.executeUpdate();
            System.out.println("Deleted notebook: " + brand);
        }
    }
}
