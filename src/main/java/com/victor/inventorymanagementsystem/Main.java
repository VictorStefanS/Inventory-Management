package com.victor.inventorymanagementsystem;

import com.victor.inventorymanagementsystem.models.Item;
import com.victor.inventorymanagementsystem.models.StoreOperationResult;
import com.victor.inventorymanagementsystem.services.Inventory;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        Inventory stock = new Inventory();
        stock.loadFromDatabase();
        while(running) {
            menu();
            int userInput = scan.nextInt();
            switch (userInput) {
                case 1:
                        handleAddItem(scan, stock);
                        stock.saveToDatabase();
                        break;
                case 2:                         //sell items
                        handleSellItem(scan, stock);
                        stock.saveToDatabase();
                        break;
                case 3:                        // list items
                    System.out.println("Here is our full inventory!");
                    System.out.println(stock);
                        break;
                case 4:
                        handleSearchItem(scan, stock);
                        break;
                case 5:
                       handleRemoveItem(scan, stock);
                       stock.saveToDatabase();
                       break;
                case 6:
                        handleUpdatePrice(scan, stock);
                        stock.saveToDatabase();
                        break;
                case 7:
                    System.out.println("Thank you for your interest!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
        scan.close();
        System.out.println("Goodbye!");

    }

    public static void menu() {
        System.out.println("Welcome to our store!");
        System.out.println("--------------");
        System.out.println("Please choose from the following:");
        System.out.println("1. Donate an item");
        System.out.println("2. Sell an item");
        System.out.println("3. List items");
        System.out.println("4. Search items");
        System.out.println("5. Remove item");
        System.out.println("6. Update price");
        System.out.println("7. Quit");
    }

    public static void handleAddItem(Scanner scan, Inventory stock){
        System.out.println(" Please fill in the required fields so your transaction gets processed");
        System.out.println("Please enter the name of the item");
        scan.nextLine();
        String itemName = scan.nextLine();
        while(itemName.isEmpty()) {
            System.out.println("Name field cannot be empty");
            System.out.println("Please enter the name of the item");
            itemName = scan.nextLine();
        }
        System.out.println("Please enter the item's price");
        double itemPrice = scan.nextDouble();
        if (itemPrice <= 0) {
            itemPrice = 0.0;
            System.out.println("Thank you for your donation!");
        }
        System.out.println("Please enter how many items you want to add");
        int itemQuantity = scan.nextInt();
        while(itemQuantity <= 0) {
            System.out.println("Quantity must be greater than 0");
            System.out.println("Please enter how many items you want to add");
            itemQuantity = scan.nextInt();
        }
        scan.nextLine();
        System.out.println("Please enter the item's category");
        String itemCategory = scan.nextLine();
        if (itemCategory.isEmpty()) {
            itemCategory = "Unknown";
        }
        System.out.println("Thank you for your interest!");
        stock.addItem(itemName, itemQuantity, itemPrice, itemCategory);
    }

    public static void handleSellItem(Scanner scan, Inventory stock){
        System.out.println("Please enter the name of the item you want to purchase");
        scan.nextLine();
        String itemName2 = scan.nextLine();
        while(itemName2.isEmpty()) {
            System.out.println("Name field cannot be empty");
            System.out.println("Please enter the name of the item you want to purchase");
            itemName2 = scan.nextLine();
        }
        System.out.println("Enter category (or press ENTER to search all):");
        String itemCategory2 = scan.nextLine();
        while(true){
            ArrayList<Item> matches = stock.searchItem(itemName2, itemCategory2);
            if(itemCategory2.isEmpty()) {
                System.out.println("Category field empty - showing all results");
                for (Item item : matches) {
                    System.out.println(item);
                }
                System.out.println("Please specify the category");
                itemCategory2 = scan.nextLine();
            } else if(matches.isEmpty()){
                System.out.println("Invalid category for this item. Please choose from the list above.");
                itemCategory2 = scan.nextLine();
            }
            if(!matches.isEmpty() && !itemCategory2.isEmpty()){
                break;
            }
        }
        System.out.println("Please enter the quantity you want to purchase");
        int itemQuantity2 = scan.nextInt();
        while(itemQuantity2 <= 0) {
            System.out.println("Quantity must be greater than 0");
            System.out.println("Please enter the quantity you want to purchase");
            itemQuantity2 = scan.nextInt();
        }
        scan.nextLine();
        StoreOperationResult result = stock.sellItem(itemName2, itemQuantity2, itemCategory2);
        switch (result) {
            case SUCCESS:
                if(itemQuantity2 == 1){
                    System.out.println("You bought one " + itemName2.toUpperCase());}
                else{System.out.println("You bought " + itemQuantity2 + " " +  itemName2.toUpperCase() + "S");}
                break;
            case NOT_FOUND:
                System.out.println("Item Not Found");
                break;
            case OUT_OF_STOCK:
                System.out.println("Out of Stock");
                break;
            case INVALID_INPUT:
                System.out.println("Invalid Input");
                break;
            case AMBIGUOUS:
                System.out.println("Category not provided. Here are all the items with the name you required");
                ArrayList<Item> matches = stock.searchItem(itemName2, "");
                for (Item item : matches) {
                    System.out.println(item);
                }
                break;
            case NOT_ENOUGH_STOCK:
                System.out.println("Not enough stock available");
                break;
            default:
                System.out.println("Something went wrong. Please try again");
                break;
        }
    }

    public static void handleSearchItem(Scanner scan, Inventory stock){
        System.out.println("Please enter the name of the item you're looking for!");
        scan.nextLine();
        String itemName3 = scan.nextLine();
        while(itemName3.isEmpty()) {
            System.out.println("Name field cannot be empty");
            System.out.println("Please enter the name of the item you're looking for");
            itemName3 = scan.nextLine();
        }
        System.out.println("Enter category (or press ENTER to search all):");
        String itemCategory3 = scan.nextLine();
        ArrayList<Item> userResults = stock.searchItem(itemName3, itemCategory3);
        if(userResults.isEmpty()){
            System.out.println("Item not found");
        } else if(userResults.size() == 1){
            System.out.println("Found " + userResults.size() + " item");
        } else{ System.out.println("Found " + userResults.size() + " items");
        }
        for(Item item : userResults){
            System.out.println(item);
        }
    }

    public static void handleRemoveItem(Scanner scan, Inventory stock){
        System.out.println("Please enter the name of the item you want to remove");
        String itemName3 = scan.nextLine();
        while(itemName3.isEmpty()) {
            System.out.println("Name field cannot be empty");
            System.out.println("Please enter the name of the item you want to purchase");
            itemName3 = scan.nextLine();
        }
        System.out.println("Enter category (or press ENTER to search all):");
        String itemCategory3 = scan.nextLine();
        while(true){
            ArrayList<Item> matches = stock.searchItem(itemName3, itemCategory3);
            if(itemCategory3.isEmpty()) {
                System.out.println("Category field empty - showing all results");
                for (Item item : matches) {
                    System.out.println(item);
                }
                System.out.println("Please specify the category");
                itemCategory3 = scan.nextLine();
            } else if(matches.isEmpty()){
                System.out.println("Invalid category for this item. Please choose from the list above.");
                itemCategory3 = scan.nextLine();
            }
            if(!matches.isEmpty() && !itemCategory3.isEmpty()){
                break;
            }
        }
        StoreOperationResult result = stock.removeItem(itemName3, itemCategory3);
        switch (result) {
            case SUCCESS:
                System.out.println("Item successfully removed");
                break;
            case NOT_FOUND:
                System.out.println("Item Not Found");
                break;
            case INVALID_INPUT:
                System.out.println("Invalid Input");
                break;
            case AMBIGUOUS:  //safety net - unreachable for now
                System.out.println("Category not provided. Here are all the items with the name you required");
                ArrayList<Item> matches = stock.searchItem(itemName3, "");
                for (Item item : matches) {
                    System.out.println(item);
                }
                break;
            default:
                System.out.println("Something went wrong. Please try again");
                break;
        }
    }
    public static void handleUpdatePrice(Scanner scan, Inventory stock){
        System.out.println("Please enter the name of the item you want to update");
        String itemName3 = scan.nextLine();
        while(itemName3.isEmpty()) {
            System.out.println("Name field cannot be empty");
            System.out.println("Please enter the name of the item you want to purchase");
            itemName3 = scan.nextLine();
        }
        System.out.println("Enter category (or press ENTER to search all):");
        String itemCategory3 = scan.nextLine();
        while(true){
            ArrayList<Item> matches = stock.searchItem(itemName3, itemCategory3);
            if(itemCategory3.isEmpty()) {
                System.out.println("Category field empty - showing all results");
                for (Item item : matches) {
                    System.out.println(item);
                }
                System.out.println("Please specify the category");
                itemCategory3 = scan.nextLine();
            } else if(matches.isEmpty()){
                System.out.println("Invalid category for this item. Please choose from the list above.");
                itemCategory3 = scan.nextLine();
            }
            if(!matches.isEmpty() && !itemCategory3.isEmpty()){
                break;
            }
        }
        System.out.println("Enter price:");
        Double userPrice = scan.nextDouble();
        while(userPrice <= 0){
            System.out.println("Invalid price - please try again");
            userPrice = scan.nextDouble();
        }
        StoreOperationResult result = stock.updatePrice(itemName3, itemCategory3,userPrice);
        switch (result) {
            case SUCCESS:
                System.out.println("Price updated successfully");
                break;
            case NOT_FOUND:
                System.out.println("Item Not Found");
                break;
            case INVALID_INPUT:
                System.out.println("Invalid Input");
                break;
            case AMBIGUOUS:
                System.out.println("Category not provided. Here are all the items with the name you required");
                ArrayList<Item> matches = stock.searchItem(itemName3, "");
                for (Item item : matches) {
                    System.out.println(item);
                }
                break;
            default:
                System.out.println("Something went wrong. Please try again");
                break;
        }
    }

}
