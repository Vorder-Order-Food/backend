package com.example.inventory_service.repository;


import com.example.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByProductIdAndQuantityIsGreaterThanEqual(String productId, Integer quantity);
    Optional<Inventory> findByProductId(String productId);
}
