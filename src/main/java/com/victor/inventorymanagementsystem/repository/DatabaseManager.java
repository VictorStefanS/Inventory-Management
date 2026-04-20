package com.victor.inventorymanagementsystem.repository;

import com.victor.inventorymanagementsystem.models.Item;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static String URL = "jdbc:sqlite:inventory.db";


    public static void setUrl(String url) {
        URL = url;
    }
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS items (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                name     TEXT    NOT NULL,
                quantity INTEGER NOT NULL,
                price    REAL    NOT NULL,
                category TEXT    NOT NULL
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize DB: " + e.getMessage());
        }
    }

    public static ArrayList<Item> loadAllItems() {
        String sql = "SELECT name, quantity, price, category FROM items";
        ArrayList<Item> items = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new Item(
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Load failed: " + e.getMessage());
        }
        return items;
    }

    public static void saveItem(Item item) {
        if(item == null){
            System.err.println("Save failed: item is null");
            return;
        }
        if(item.getName().isEmpty() || item.getCategory().isEmpty()){
            System.err.println("Save failed: name and category must be specified");
            return;
        }
        if(item.getQuantity() <= 0){
            System.err.println("Save failed: quantity must be greater than 0");
            return;
        }
        String checkSql = "SELECT id FROM items WHERE name = ? AND category = ?";
        String updateSql = "UPDATE items SET quantity = ? WHERE name = ? AND category = ?";
        String insertSql = "INSERT INTO items (name, quantity, price, category) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect()) {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setString(1, item.getName());
            check.setString(2, item.getCategory());
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement update = conn.prepareStatement(updateSql);
                update.setInt(1, item.getQuantity());
                update.setString(2, item.getName());
                update.setString(3, item.getCategory());
                update.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement(insertSql);
                insert.setString(1, item.getName());
                insert.setInt(2, item.getQuantity());
                insert.setDouble(3, item.getPrice());
                insert.setString(4, item.getCategory());
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    public static void deleteItem(Item item) {
        if(item == null){
            System.err.println("Delete failed: item is null");
            return;
        }
        if(item.getName().isEmpty() || item.getCategory().isEmpty()){
            System.err.println("Delete failed: name and category must be specified");
            return;
        }
        String checkSql = "DELETE FROM items WHERE name = ? AND category = ?";
        try(Connection conn = connect()){
            PreparedStatement update = conn.prepareStatement(checkSql);
            update.setString(1, item.getName());
            update.setString(2, item.getCategory());
            update.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }
    }

    public static void updatePrice(Item item) {
        if(item == null){
            System.err.println("Update failed: item is null");
            return;
        }
        if(item.getName().isEmpty() || item.getCategory().isEmpty()){
            System.err.println("Update failed: name and category must be specified");
            return;
        }
        if(item.getPrice() <= 0){
            System.err.println("Update failed: price must be greater than 0");
            return;
        }
        String updateSql = "UPDATE items SET price = ? WHERE name = ? AND category = ?";
        try(Connection conn = connect()){
            PreparedStatement update = conn.prepareStatement(updateSql);
            update.setDouble(1, item.getPrice());
            update.setString(2, item.getName());
            update.setString(3, item.getCategory());
            update.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }
}
