package com.victor.inventorymanagementweb.repository;

import com.victor.inventorymanagementweb.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Item entity.
 * Provides database access methods for item operations.
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Finds items by name (case-insensitive).
     */
    List<Item> findByNameIgnoreCase(String name);

    /**
     * Finds items by name and category (case-insensitive).
     */
    List<Item> findByNameIgnoreCaseAndCategoryIgnoreCase(String name, String category);
}
