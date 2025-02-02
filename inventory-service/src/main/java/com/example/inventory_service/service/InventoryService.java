package com.example.inventory_service.service;

import com.example.inventory_service.dto.request.IsInStockRequest;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    InventoryRepository inventoryRepository;

    public boolean isInStock(@RequestParam String productId, @RequestParam int quantity) {
        return inventoryRepository.existsByProductIdAndQuantityIsGreaterThanEqual(
                productId, quantity
        );
    }

}
