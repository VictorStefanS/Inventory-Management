package services;

import com.victor.inventorymanagementsystem.models.Item;
import com.victor.inventorymanagementsystem.models.StoreOperationResult;
import com.victor.inventorymanagementsystem.services.Inventory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.victor.inventorymanagementsystem.models.StoreOperationResult.*;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    @Test
    public void testAddItem() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        Item item = results.get(0);
        assertEquals(1, results.size());
        assertEquals("item1", item.getName());
        assertEquals(20.0, item.getPrice());
        assertEquals("general", item.getCategory());
        assertEquals(60, item.getQuantity());
    }

    @Test
    public void testAddItem_existingItem_increaseQuantity() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        inventory.addItem("item1", 60, 20.0, "general");
        inventory.addItem("item1", 10, 20.0, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        Item item = results.get(0);
        assertEquals(130, item.getQuantity());
    }

    @Test
    public void testAddItem_invalidQuantity_isIgnored() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", -3, 20.0, "general");
        inventory.addItem("item1", -10, 20.0, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(0,  results.size());
    }

    @Test
    public void testSearchItem_validInput_returnsCorrectItem(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(1, results.size());
        Item item = results.get(0);
        assertEquals("item1",  item.getName());
        assertEquals(20.0, item.getPrice());
        assertEquals(60, item.getQuantity());
        assertEquals("general", item.getCategory());
    }

    @Test
    public void testSearchItem_without_category() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "electronics");
        inventory.addItem("item1", 60, 20.0, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "");
        assertEquals(2,  results.size());
    }

    @Test
    public void testSearchItem_with_non_existing_Item(){
        Inventory inventory = new Inventory();
        ArrayList<Item> results = inventory.searchItem("item2", "general");
        assertEquals(0,  results.size());
    }

    @Test
    public void testSellItem_blankInput_INVALID_INPUT(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.sellItem("", -2, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(INVALID_INPUT,  result);
        assertEquals(1, results.size());
        Item item = results.get(0);
        assertEquals(60, item.getQuantity());
    }

    @Test
    public void testSellItem_itemNotInInventory_NOT_FOUND(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.sellItem("item2", 20, "general");
        ArrayList<Item> results_userInput = inventory.searchItem("item2", "general");
        ArrayList<Item> results_currentInventory = inventory.searchItem("item1", "general");
        assertEquals(NOT_FOUND, result);
        assertEquals(0,  results_userInput.size());
        assertEquals(1, results_currentInventory.size());
        Item item = results_currentInventory.get(0);
        assertEquals(60, item.getQuantity());
    }

    @Test
    public void testSellItem_categoryNotProvided_AMBIGUOUS(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        inventory.addItem("item1", 10, 20.0, "electronics");
        StoreOperationResult result = inventory.sellItem("item1", 10,"");
        ArrayList<Item> results = inventory.searchItem("item1", "");
        assertEquals(AMBIGUOUS, result);
        assertEquals(2,  results.size());
        boolean isGeneral = false;
        boolean isElectronics = false;
        for(Item item : results){
            if(item.getCategory().equals("electronics")){
                assertEquals(10, item.getQuantity());
                isElectronics = true;
            }
            else if(item.getCategory().equals("general")){
                assertEquals(60, item.getQuantity());
                isGeneral = true;
            }
        }
        assertTrue(isGeneral);
        assertTrue(isElectronics);
    }

    @Test
    public void testSellItem_quantityIs0_OUT_OF_STOCK(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 10, 20.0, "general");
        inventory.sellItem("item1", 10, "general");
        StoreOperationResult result = inventory.sellItem("item1", 1,"general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(OUT_OF_STOCK, result);
        assertEquals(1,  results.size());
        Item item = results.get(0);
        assertEquals(0, item.getQuantity());
    }

    @Test
    public void testSellItem_userQuantityBiggerThanStock_NOT_ENOUGH_STOCK(){
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.sellItem("item1", 100, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(NOT_ENOUGH_STOCK, result);
        assertEquals(1,  results.size());
        Item item = results.get(0);
        assertEquals(60,  item.getQuantity());
    }

    @Test
    public void testSellItem_validInput_reducesQuantity_SUCCESS() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.sellItem("item1", 10, "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(SUCCESS, result);
       assertEquals(1, results.size());
       Item item = results.get(0);
       assertEquals(50, item.getQuantity());
    }

    @Test
     public void testRemoveItem_validInput_removesItem_SUCCESS() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.removeItem("item1",  "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(SUCCESS, result);
        assertEquals(0, results.size());
     }

     @Test
     public void testRemoveItem_INVALID_INPUT() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.removeItem("", "general");
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(INVALID_INPUT, result);
        assertEquals(1, results.size());
     }

     @Test
     public void testRemoveItem_NOT_FOUND() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.removeItem("item2", "general");
        assertEquals(NOT_FOUND, result);
     }

     @Test
     public void testRemoveItem_AMBIGUOUS() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        inventory.addItem("item1", 10, 20.0, "electronics");
        StoreOperationResult result = inventory.removeItem("item1", "");
        ArrayList<Item> results = inventory.searchItem("item1", "");
        assertEquals(AMBIGUOUS, result);
        assertEquals(2, results.size());
     }

     @Test
     public void testUpdatePrice_INVALID_INPUT() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.updatePrice("item1", "general", -20.0);
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(INVALID_INPUT, result);
        assertEquals(1, results.size());
        Item item = results.get(0);
        assertEquals(20.0, item.getPrice());
     }

     @Test
     public void testUpdatePrice_NOT_FOUND() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.updatePrice("item2", "general", 30.0);
        ArrayList<Item> results = inventory.searchItem("item2", "general");
        ArrayList<Item> initial_results = inventory.searchItem("item1", "general");
        assertEquals(NOT_FOUND, result);
        assertEquals(0, results.size());
        assertEquals(1,  initial_results.size());
        Item item = initial_results.get(0);
        assertEquals(20.0, item.getPrice());
     }

     @Test
    public void testUpdatePrice_AMBIGUOUS() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        inventory.addItem("item1", 10, 40.0, "electronics");
        StoreOperationResult result = inventory.updatePrice("item1", "", 30.0);
        assertEquals(AMBIGUOUS, result);
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(1, results.size());
        Item item = results.get(0);
        assertEquals(20.0, item.getPrice());
        ArrayList<Item> additional_results = inventory.searchItem("item1", "electronics");
        assertEquals(1, additional_results.size());
        Item item2 = additional_results.get(0);
        assertEquals(40.0, item2.getPrice());
     }

     @Test
     public void testUpdatePrice_SUCCESS() {
        Inventory inventory = new Inventory();
        inventory.addItem("item1", 60, 20.0, "general");
        StoreOperationResult result = inventory.updatePrice("item1", "general", 30.0);
        ArrayList<Item> results = inventory.searchItem("item1", "general");
        assertEquals(SUCCESS, result);
        assertEquals(1, results.size());
        Item item = results.get(0);
        assertEquals(30.0, item.getPrice());
     }


}
