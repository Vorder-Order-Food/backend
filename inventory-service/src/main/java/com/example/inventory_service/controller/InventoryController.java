package com.example.inventory_service.controller;

import com.example.inventory_service.dto.request.IsInStockRequest;
import com.example.inventory_service.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryController {

    InventoryService inventoryService;

    @GetMapping("/isInStock")
    public boolean isInStock(@RequestParam String productId, @RequestParam int quantity) {
        return inventoryService.isInStock(productId, quantity);
    }
}
