package com.example.inventory_service.dto;

import lombok.Data;

@Data
public class InventoryDto {
    Long id;

    String productId;
    Integer quantity;
}
