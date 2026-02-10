package csd214.bookstore.mysql;

import csd214.bookstore.pojos.*;
import java.sql.*;

public class JdbcPhoneApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASS = "itstudies12345";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Create Table
            createTable(conn);
            // 2. Insert
            System.out.println("--- INSERTING ---");
            Phone p1 = new Phone("iPhone", true, 999.99, 8);
            insertPhone(conn, p1);
            // 3. Read
            System.out.println("--- READING ---");
            listPhones(conn);
            // 4. Update
            System.out.println("--- UPDATING ---");
            updatePhonePrice(conn, "iPhone", true, 899.99, 8);

            // 5. Delete
            System.out.println("--- DELETING ---");
            deletePhone(conn, "iPhone", true, 8);
            listPhones(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS phones (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id VARCHAR(36), " +
                "brand VARCHAR(255), " +
                "support5G BIT," +
                "price DOUBLE," +
                "copies INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'phones' ready.");
        }
    }
    private static void insertPhone(Connection conn, Phone p) throws SQLException {
        // SECURITY: Use ? to prevent SQL Injection
        String sql = "INSERT INTO phones (product_id, brand, supports5G, price, copies) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductId()); // UUID
            ps.setString(2, p.getBrand());
            ps.setBoolean(3, p.isSupports5G());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getCopies());
            ps.executeUpdate();
            System.out.println("Saved: " + p.getBrand());
        }
    }
    private static void listPhones(Connection conn) throws SQLException {
        String sql = "SELECT * FROM phones";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | UUID: %s | Brand: %s | Supports 5G? %b | Price: $%.2f%n | Copies: %d",
                        rs.getInt("id"),
                        rs.getString("product_id"),
                        rs.getString("brand"),
                        rs.getBoolean("supports5G"),
                        rs.getDouble("price"),
                        rs.getInt("copies"));
            }
        }
    }
    private static void updatePhonePrice(Connection conn, String brand, boolean supports5G, double newPrice, int copies) throws SQLException {
        String sql = "UPDATE phones SET price = ? WHERE brand = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setString(2, brand);
            ps.setBoolean(3, supports5G);
            ps.setInt(4, copies);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " phone(s).");
        }
    }

    private static void deletePhone(Connection conn, String brand, boolean supports5G, int copies) throws SQLException {
        String sql = "DELETE FROM phones WHERE brand = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.setBoolean(2, supports5G);
            ps.setInt(3, copies);
            ps.executeUpdate();
            System.out.println("Deleted phone: " + brand);
        }
    }
}