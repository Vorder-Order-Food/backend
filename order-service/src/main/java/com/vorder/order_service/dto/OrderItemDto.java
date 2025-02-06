package com.vorder.order_service.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;

    private String productId;

    private String productName;
    private Long productPrice;

    private int quantity;

    private Long totalPrice;
}
