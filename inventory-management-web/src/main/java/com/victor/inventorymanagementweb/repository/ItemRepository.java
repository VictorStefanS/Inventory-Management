package com.victor.inventorymanagementweb.repository;

import com.victor.inventorymanagementweb.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameIgnoreCase(String name);
    List<Item> findByNameIgnoreCaseAndCategoryIgnoreCase(String name, String category);
}
