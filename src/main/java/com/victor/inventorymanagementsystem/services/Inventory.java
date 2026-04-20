package com.victor.inventorymanagementsystem.services;
import com.victor.inventorymanagementsystem.models.Item;
import com.victor.inventorymanagementsystem.models.StoreOperationResult;
import com.victor.inventorymanagementsystem.repository.DatabaseManager;
import java.util.ArrayList;


public class Inventory {
    private ArrayList<Item> stock;
    private int nextId = 1;

    public Inventory() {
        stock = new ArrayList<>();
    }

    public ArrayList<Item> getStock() {
        return stock;
    }

    public void addItem(String name, int quantity, double price, String category) {
        if(name == null) {
            return;
        }
        name = name.trim();
        if(name.isEmpty()) {
            return;
        }
        if(quantity <= 0) {
            return;
        }
        if(price <= 0) {
            price = 0.0;
        }
        if(category == null) {
            category = "Unknown";
        }
        category = category.trim();
        if(category.isEmpty()) {
            category = "Unknown";
        }
        for (Item item : stock) {
            if(item.getName().trim().equalsIgnoreCase(name) && item.getCategory().trim().equalsIgnoreCase(category)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }

        }
        Item item1 = new Item( name, quantity, price, category);
        stock.add(item1);
    }

    public StoreOperationResult sellItem(String userInputProduct, int userInputQuantity, String userInputCategory) {
            if (userInputProduct.isBlank() || userInputQuantity <= 0) {
                return StoreOperationResult.INVALID_INPUT;
            }
            ArrayList<Item> matches = searchItem(userInputProduct, userInputCategory);
            if(matches.isEmpty()){
                return StoreOperationResult.NOT_FOUND;
            }
            if(userInputCategory.isBlank() && matches.size() > 1 ){
                return StoreOperationResult.AMBIGUOUS;
            }
            Item item = matches.get(0);
            if(item.getQuantity() == 0){
                return StoreOperationResult.OUT_OF_STOCK;
            }
            if(item.getQuantity() < userInputQuantity){
                return StoreOperationResult.NOT_ENOUGH_STOCK;
            }
                item.setQuantity(item.getQuantity() - userInputQuantity);
                return StoreOperationResult.SUCCESS;
        }

    public ArrayList<Item> searchItem(String userInputProduct, String userInputCategory) {
        ArrayList<Item> matches = new ArrayList<>();
        if (userInputProduct.isBlank()) {
            return matches;
        }
        for (Item item : stock) {
            if(userInputCategory.isBlank()) {
                if (item.getName().equalsIgnoreCase(userInputProduct.trim())) {
                    matches.add(item);
                }
            } else if(item.getName().equalsIgnoreCase(userInputProduct.trim()) && userInputCategory.equalsIgnoreCase(item.getCategory().trim())) {
                matches.add(item);
            }
        }
        return matches;
    }

    public void saveToDatabase() {
        for (Item item : stock) {
            DatabaseManager.saveItem(item);
        }
    }

    public void loadFromDatabase() {
        stock.clear();
        stock.addAll(DatabaseManager.loadAllItems());
    }

    public StoreOperationResult removeItem(String userInputProduct, String userInputCategory) {
        if (userInputProduct.isBlank()) {
            return StoreOperationResult.INVALID_INPUT;
        }
        ArrayList<Item> matches = searchItem(userInputProduct, userInputCategory);
        if(matches.isEmpty()){
            return StoreOperationResult.NOT_FOUND;
        }
        if(userInputCategory.isBlank() && matches.size() > 1 ){
            return StoreOperationResult.AMBIGUOUS;
        }
        Item item = matches.get(0);
        stock.remove(item);
        DatabaseManager.deleteItem(item);
        return StoreOperationResult.SUCCESS;
    }

    public StoreOperationResult updatePrice(String userInputProduct, String userInputCategory, Double userInputPrice) {
        if (userInputProduct.isBlank() || userInputPrice <= 0) {
            return  StoreOperationResult.INVALID_INPUT;
        }
        ArrayList<Item> matches = searchItem(userInputProduct, userInputCategory);
        if(matches.isEmpty()){
            return  StoreOperationResult.NOT_FOUND;
        }
         if(userInputCategory.isBlank() && matches.size() > 1 ){
             return StoreOperationResult.AMBIGUOUS;
         }
         Item item = matches.get(0);
         item.setPrice(userInputPrice);
         DatabaseManager.updatePrice(item);
         return StoreOperationResult.SUCCESS;
    }

    @Override
    public String toString() {
        String str = "";
        for (Item b : stock) {
            str +=  "Product: " + b.getName() + "\n"
                    + "Quantity: " + b.getQuantity() + "\n"
                    + "Price: " + b.getPrice() + "\n"
                    +  "Category: " +b.getCategory() + "\n"
                    + "\n";
        }
        return str;
    }
}
