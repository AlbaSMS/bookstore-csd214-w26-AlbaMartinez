package csd214.bookstore.mysql;
import csd214.bookstore.pojos.Laptop;
import csd214.bookstore.pojos.Widget;
import java.sql.*;

public class JdbcLaptopApp {
    private static final String URL = "jdbc:mysql://localhost:3333/bookstore";
    private static final String USER = "csd214";
    private static final String PASS = "itstudies12345";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            // 1. Create TableV
            createTable(conn);
            // 2. Insert
            System.out.println("--- INSERTING ---");
            Laptop l1 = new Laptop("Lenovo", 16.5,12);
            insertLaptop(conn, l1);
            // 3. Read
            System.out.println("--- READING ---");
            listLaptop(conn);
            // 4. Update
            System.out.println("--- UPDATING ---");
            updateLaptopWarrantyMonths(conn, "Lenovo", 16.5, 18);

            // 5. Delete
            System.out.println("--- DELETING ---");
            deleteLaptop(conn, "Lenovo");
            listLaptop(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS laptops (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id VARCHAR(36), " +
                "brand VARCHAR(255), " +
                "screenSizeInches DOUBLE)" +
                "warrantyMonths DOUBLE)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'laptops' ready.");
        }
    }
    private static void insertLaptop(Connection conn, Laptop l) throws SQLException {
        // SECURITY: Use ? to prevent SQL Injection
        String sql = "INSERT INTO laptops (product_id, brand, screenSizeInches, warrantyMonths) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getProductId()); // UUID
            ps.setString(2, l.getBrand());
            ps.setDouble(3, l.getScreenSizeInches());
            ps.setDouble(4, l.getWarrantyMonths());
            ps.executeUpdate();
            System.out.println("Saved: " + l.getBrand());
        }
    }
    private static void listLaptop(Connection conn) throws SQLException {
        String sql = "SELECT * FROM laptops";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d | UUID: %s | Brand: %s | Screen Size Inches: $%.2f%n | Warranty Months: $%.2f%n",
                        rs.getInt("id"),
                        rs.getString("product_id"),
                        rs.getString("laptop_brand"),
                        rs.getDouble("warranty_months"),
                        rs.getDouble("screen_size_inches"));
            }
        }
    }
    private static void updateLaptopWarrantyMonths(Connection conn, String brand, double screenSizeInches, double newWarrantyMonths) throws SQLException {
        String sql = "UPDATE laptops SET warrantyMonths = ? WHERE laptop_brand = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newWarrantyMonths);
            ps.setString(2, brand);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " laptop(s).");
        }
    }

    private static void deleteLaptop(Connection conn, String brand) throws SQLException {
        String sql = "DELETE FROM laptops WHERE laptop_brand = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ps.executeUpdate();
            System.out.println("Deleted laptop: " + brand);
        }
    }
}