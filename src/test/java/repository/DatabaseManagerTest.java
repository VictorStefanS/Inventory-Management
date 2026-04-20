package repository;

import com.victor.inventorymanagementsystem.models.Item;
import com.victor.inventorymanagementsystem.repository.DatabaseManager;
import com.victor.inventorymanagementsystem.services.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @BeforeEach
    public void setUp() {
        DatabaseManager.setUrl("jdbc:sqlite:test_inventory.db");
        DatabaseManager.initializeDatabase();
    }

    @AfterEach
    public void tearDown() {
        new java.io.File("test_inventory.db").delete();
    }

    @Test
    public void testConnection() {
        assertDoesNotThrow(() -> {
            Connection conn = DatabaseManager.connect();
            assertNotNull(conn);
            conn.close();
        });
    }

    @Test
    public void testInitializeDatabase() {
        assertDoesNotThrow(() -> DatabaseManager.initializeDatabase());
    }

    @Test
    public void testSaveItem_newItem_canBeLoaded() {
        DatabaseManager.initializeDatabase();
        Item item = new Item("testItem", 10, 5.0, "general");
        DatabaseManager.saveItem(item);
        ArrayList<Item> items = DatabaseManager.loadAllItems();
        assertTrue(items.stream().anyMatch(i -> i.getName().equals("testItem")));
    }
    @Test
    public void testSaveToDatabase_andLoadFromDatabase_roundTrip() {
        Inventory inventory = new Inventory();
        inventory.addItem("testItem", 10, 5.0, "general");
        inventory.saveToDatabase();

        Inventory inventory2 = new Inventory();
        inventory2.loadFromDatabase();
        ArrayList<Item> items = inventory2.getStock();

        assertTrue(items.stream().anyMatch(i ->
                i.getName().equalsIgnoreCase("testItem") &&
                        i.getCategory().equalsIgnoreCase("general") &&
                        i.getQuantity() == 10 &&
                        i.getPrice() == 5.0
        ));
    }

    @Test
    public void testLoadFromDatabase_emptyDatabase_returnsEmptyStock() {
        Inventory inventory = new Inventory();
        inventory.loadFromDatabase();
        assertNotNull(inventory.getStock());
    }

    @Test
    public void testDeleteItem_existingItem_isDeleted() {
        Item item = new Item("testItem", 10, 5.0, "general");
        DatabaseManager.saveItem(item);
        DatabaseManager.deleteItem(item);
        ArrayList<Item> items = DatabaseManager.loadAllItems();
        assertFalse(items.stream().anyMatch(i -> i.getName().equals("testItem")));
    }

    @Test
    public void testDeleteItem_nonExistingItem_doesNothing() {
        Item item = new Item("ghost", 10, 5.0, "general");
        DatabaseManager.deleteItem(item);
        ArrayList<Item> items = DatabaseManager.loadAllItems();
        assertFalse(items.stream().anyMatch(i -> i.getName().equals("ghost")));
    }

    @Test
    public void testUpdatePrice(){
        Item item = new Item("testItem", 10, 5.0, "general");
        DatabaseManager.saveItem(item);
        item.setPrice(99.0);
        DatabaseManager.updatePrice(item);
        ArrayList<Item> items = DatabaseManager.loadAllItems();
        assertTrue(items.stream().anyMatch(i -> i.getPrice() == 99.0));
    }
}