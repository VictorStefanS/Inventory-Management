package com.victor.inventorymanagementweb.controllers;

import com.victor.inventorymanagementweb.models.Item;
import com.victor.inventorymanagementweb.models.OperationResult;
import com.victor.inventorymanagementweb.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items", inventoryService.getAllItems());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String name,
                         @RequestParam(required = false) String category,
                         Model model) {
        List<Item> results = inventoryService.searchItems(name, category);
        model.addAttribute("items", results);
        return "index";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam String name,
                          @RequestParam int quantity,
                          @RequestParam double price,
                          @RequestParam String category,
                          RedirectAttributes redirectAttributes) {
        OperationResult result = inventoryService.addItem(name, quantity, price, category);
        switch (result) {
            case QUANTITY_UPDATED -> redirectAttributes.addFlashAttribute("message",
                    "Quantity updated. To change the price use the Update Price option.");
            case ITEM_ADDED ->   redirectAttributes.addFlashAttribute("message", "Item has been added");
            case NEGATIVE_QUANTITY ->   redirectAttributes.addFlashAttribute("message", "Quantity has to be greater than 0");
            case INVALID_NAME -> redirectAttributes.addFlashAttribute("message", "Invalid name");
            case INVALID_PRICE ->  redirectAttributes.addFlashAttribute("message", "Price has to be greater than 0");
            case GENERAL_FAILURE ->   redirectAttributes.addFlashAttribute("message", "Operation failed");
        }
        return "redirect:/";
    }

    @PostMapping("/sell")
    public String sellItem(@RequestParam int quantity,
                           @RequestParam String category,
                           @RequestParam String name,
                           RedirectAttributes redirectAttributes) {
        OperationResult result = inventoryService.sellItem(name, quantity, category);
        switch (result) {
            case ITEM_SOLD ->  redirectAttributes.addFlashAttribute("message", "Transaction processed successfully");
            case NOT_FOUND ->    redirectAttributes.addFlashAttribute("message", "Item Not Found");
            case NEGATIVE_QUANTITY ->    redirectAttributes.addFlashAttribute("message", "Quantity has to be greater than 0");
            case AMBIGUOUS ->     redirectAttributes.addFlashAttribute("message", "Please specify the category");
            case OUT_OF_STOCK ->    redirectAttributes.addFlashAttribute("message", "Out of stock");
            case NOT_ENOUGH_STOCK ->     redirectAttributes.addFlashAttribute("message", "Not enough stock");
            case GENERAL_FAILURE ->     redirectAttributes.addFlashAttribute("message", "Operation failed");
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        boolean result = inventoryService.deleteItem(id);
        if(!result) {
            redirectAttributes.addFlashAttribute("message", "Operation failed");
        } else {
        redirectAttributes.addFlashAttribute("message", "Item has been deleted");}
        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String updatePrice(@PathVariable Long id,
                              @RequestParam double price,
                              RedirectAttributes redirectAttributes) {

        boolean result = inventoryService.updatePrice(id, price);
        if (!result) {
            redirectAttributes.addFlashAttribute("message", "Price has to be greater than 0");
        } else {redirectAttributes.addFlashAttribute("message", "Price has been updated");}
        return "redirect:/";
    }
}