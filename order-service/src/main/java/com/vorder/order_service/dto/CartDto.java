package com.vorder.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private Long id;
    private String userId;
    private Long total;
    private List<CartItemDto> items;
}
