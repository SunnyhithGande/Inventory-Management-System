import java.sql.*;
import java.util.Scanner;

public class InventoryManagementSystem {
    static final String URL = "jdbc:mysql://localhost:3306/inventorydb";
    static final String USER = "root"; 
    static final String PASS = "123456"; 
    static Connection conn;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            menu();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void menu() throws SQLException {
        while (true) {
            System.out.println("\n===== Inventory Management =====");
            System.out.println("1. Add Supplier");
            System.out.println("2. Add Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. List Products");
            System.out.println("6. Search Product by Name");
            System.out.println("7. Place Order");
            System.out.println("8. Low Stock Alert");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1: addSupplier(); break;
                case 2: addProduct(); break;
                case 3: updateProduct(); break;
                case 4: deleteProduct(); break;
                case 5: listProducts(); break;
                case 6: searchProduct(); break;
                case 7: placeOrder(); break;
                case 8: lowStockAlert(); break;
                case 0: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static void addSupplier() throws SQLException {
        System.out.print("Supplier Name: ");
        String name = sc.nextLine();
        System.out.print("Contact: ");
        String contact = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO suppliers(name, contact) VALUES (?, ?)");
        ps.setString(1, name);
        ps.setString(2, contact);
        ps.executeUpdate();
        System.out.println("✅ Supplier Added!");
    }

    static void addProduct() throws SQLException {
        System.out.print("Product Name: ");
        String name = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Price: ");
        double price = sc.nextDouble(); sc.nextLine();
        System.out.print("Supplier ID: ");
        int sid = sc.nextInt(); sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO products(name, quantity, price, supplier_id) VALUES (?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setInt(2, qty);
        ps.setDouble(3, price);
        ps.setInt(4, sid);
        ps.executeUpdate();
        System.out.println("✅ Product Added!");
    }

    static void updateProduct() throws SQLException {
        System.out.print("Product ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Quantity: ");
        int qty = sc.nextInt();
        System.out.print("New Price: ");
        double price = sc.nextDouble(); sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("UPDATE products SET name=?, quantity=?, price=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, qty);
        ps.setDouble(3, price);
        ps.setInt(4, id);
        ps.executeUpdate();
        System.out.println("✅ Product Updated!");
    }

    static void deleteProduct() throws SQLException {
        System.out.print("Product ID: ");
        int id = sc.nextInt();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id=?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("🗑 Product Deleted!");
    }

    static void listProducts() throws SQLException {
        String sql = "SELECT p.id, p.name, p.quantity, p.price, s.name AS supplier FROM products p " +
                     "LEFT JOIN suppliers s ON p.supplier_id=s.id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("name") +
                    " | Qty: " + rs.getInt("quantity") +
                    " | Price: " + rs.getDouble("price") +
                    " | Supplier: " + rs.getString("supplier"));
        }
    }

    static void searchProduct() throws SQLException {
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE name LIKE ?");
        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("name") +
                    " | Qty: " + rs.getInt("quantity") +
                    " | Price: " + rs.getDouble("price"));
        }
    }

    static void placeOrder() throws SQLException {
        System.out.print("Product ID: ");
        int pid = sc.nextInt();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();

        PreparedStatement psCheck = conn.prepareStatement("SELECT quantity, price FROM products WHERE id=?");
        psCheck.setInt(1, pid);
        ResultSet rs = psCheck.executeQuery();
        if (rs.next()) {
            int stock = rs.getInt("quantity");
            double price = rs.getDouble("price");

            if (stock >= qty) {
                double total = price * qty;

                PreparedStatement psOrder = conn.prepareStatement("INSERT INTO orders(product_id, quantity, total_price) VALUES (?, ?, ?)");
                psOrder.setInt(1, pid);
                psOrder.setInt(2, qty);
                psOrder.setDouble(3, total);
                psOrder.executeUpdate();

                PreparedStatement psUpdate = conn.prepareStatement("UPDATE products SET quantity=quantity-? WHERE id=?");
                psUpdate.setInt(1, qty);
                psUpdate.setInt(2, pid);
                psUpdate.executeUpdate();

                System.out.println("✅ Order Placed! Total Price: " + total);
            } else {
                System.out.println("❌ Not enough stock!");
            }
        } else {
            System.out.println("❌ Product not found!");
        }
    }

    static void lowStockAlert() throws SQLException {
        System.out.print("Enter low-stock threshold: ");
        int threshold = sc.nextInt();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE quantity < ?");
        ps.setInt(1, threshold);
        ResultSet rs = ps.executeQuery();
        System.out.println("⚠ Low Stock Products:");
        while (rs.next()) {
            System.out.println(rs.getString("name") + " | Qty: " + rs.getInt("quantity"));
        }
    }
}
