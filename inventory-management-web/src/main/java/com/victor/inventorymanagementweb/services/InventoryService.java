package com.victor.inventorymanagementweb.services;

import com.victor.inventorymanagementweb.models.Item;
import com.victor.inventorymanagementweb.models.OperationResult;
import com.victor.inventorymanagementweb.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for inventory operations.
 * Handles business logic for adding, selling, updating, and managing items.
 */
@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Retrieves all items from the inventory.
     */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * Searches for items by name and optionally by category.
     */
    public List<Item> searchItems(String name, String category) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        if (category == null || category.isBlank()) {
            return itemRepository.findByNameIgnoreCase(name);
        }
        return itemRepository.findByNameIgnoreCaseAndCategoryIgnoreCase(name, category);
    }

    /**
     * Adds a new item or updates quantity if item already exists.
     */
    public OperationResult addItem(String name, int quantity, double price, String category) {
        if (name == null || name.isBlank() || name.length() > 100) {
            return OperationResult.INVALID_NAME;
        }
        if (quantity <= 0) {
            return OperationResult.NEGATIVE_QUANTITY;
        }
        if (price <= 0) {
            return OperationResult.INVALID_PRICE;
        }
        if (category == null || category.isBlank()) {
            category = "Unknown";
        }
        List<Item> matches = itemRepository.findByNameIgnoreCaseAndCategoryIgnoreCase(name, category);
        try {
            if (!matches.isEmpty()) {
                Item existing = matches.get(0);
                existing.setQuantity(existing.getQuantity() + quantity);
                itemRepository.save(existing);
                return OperationResult.QUANTITY_UPDATED;
            } else {
                Item newItem = new Item(name, quantity, price, category);
                itemRepository.save(newItem);
                return OperationResult.ITEM_ADDED;
            }
        } catch (Exception e) {
            return OperationResult.GENERAL_FAILURE;
        }
    }

    /**
     * Processes a sale transaction by reducing item quantity.
     */
    public OperationResult sellItem(String name, int quantity, String category) {
        if (quantity <= 0) {
            return OperationResult.NEGATIVE_QUANTITY;
        }
        try {
            if (category == null || category.isBlank()) {
                List<Item> allMatches = itemRepository.findByNameIgnoreCase(name);
                if (allMatches.size() > 1) {
                    return OperationResult.AMBIGUOUS;
                }
            }
            List<Item> matches = itemRepository.findByNameIgnoreCaseAndCategoryIgnoreCase(name, category);
            if (matches.isEmpty()) {
                return OperationResult.NOT_FOUND;
            }
            Item item = matches.get(0);
            if (item.getQuantity() == 0) {
                return OperationResult.OUT_OF_STOCK;
            }
            if (item.getQuantity() < quantity) {
                return OperationResult.NOT_ENOUGH_STOCK;
            }
            item.setQuantity(item.getQuantity() - quantity);
            itemRepository.save(item);
            return OperationResult.ITEM_SOLD;
        } catch (Exception e) {
            return OperationResult.GENERAL_FAILURE;
        }
    }

    /**
     * Deletes an item from the inventory.
     */
    public boolean deleteItem(Long id) {
        try {
            itemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Updates the price of an existing item.
     */
    public boolean updatePrice(Long id, double newPrice) {
        if (newPrice <= 0) {
            return false;
        }
        return itemRepository.findById(id).map(item -> {
            item.setPrice(newPrice);
            itemRepository.save(item);
            return true;
        }).orElse(false);
    }
}