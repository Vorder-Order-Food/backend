package com.vorder.cart_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;
    private String name;
    private Long price;
    private List<String> images;
}
