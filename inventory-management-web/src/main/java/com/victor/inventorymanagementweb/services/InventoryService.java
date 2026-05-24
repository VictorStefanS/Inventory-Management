package com.victor.inventorymanagementweb.services;

import com.victor.inventorymanagementweb.models.Item;
import com.victor.inventorymanagementweb.models.OperationResult;
import com.victor.inventorymanagementweb.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> searchItems(String name, String category) {
        if (name == null || name.isBlank()) {
            return List.of();
        }
        if (category == null || category.isBlank()) {
            return itemRepository.findByNameIgnoreCase(name);
        }
        return itemRepository.findByNameIgnoreCaseAndCategoryIgnoreCase(name, category);
    }

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

    public OperationResult sellItem(String name, int quantity, String category) {
        if(quantity<=0){
            return OperationResult.NEGATIVE_QUANTITY;
        }
        try {
        if(category == null || category.isBlank()){
            List<Item> allMatches = itemRepository.findByNameIgnoreCase(name);
            if(allMatches.size() > 1){
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

    public boolean deleteItem(Long id) {
        try{
            itemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePrice(Long id, double newPrice) {
        if(newPrice <= 0) {
            return false;
        }
        itemRepository.findById(id).ifPresent(item -> {
            item.setPrice(newPrice);
            itemRepository.save(item);
        });
        return true;
    }
}