package com.vorder.product_service.controller;

import com.vorder.product_service.dto.ApiResponse;
import com.vorder.product_service.dto.ProductDto;
import com.vorder.product_service.dto.request.ProductRequest;
import com.vorder.product_service.dto.response.ProductResponse;
import com.vorder.product_service.entity.Product;
import com.vorder.product_service.mapper.ProductMapper;
import com.vorder.product_service.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;
    ProductMapper productMapper;

    @PostMapping("/create")
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/find-all")
    ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getFoodById(@PathVariable String id)  {
        Product food = productService.findById(id).orElseThrow(RuntimeException::new);
        ProductDto foodDto = productMapper.productToProductDto(food);
        return ResponseEntity.ok(foodDto);
    }
}
