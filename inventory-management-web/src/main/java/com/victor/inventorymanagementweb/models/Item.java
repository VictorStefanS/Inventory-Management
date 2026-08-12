package com.victor.inventorymanagementweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name cannot be empty")
    @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
    private String name;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 2 decimal places")
    private double price;

    @NotBlank(message = "Category cannot be empty")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    public Item() {}

    public Item(String name, int quantity, double price, String category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

}
