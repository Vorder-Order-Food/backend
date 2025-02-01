package com.vorder.order_service.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private String productId;
    private String productName;
    private Long productPrice;
    private Integer quantity;
    private Long totalPrice;
}
