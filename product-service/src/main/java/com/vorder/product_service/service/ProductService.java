package com.vorder.product_service.service;

import com.vorder.product_service.dto.request.ProductRequest;
import com.vorder.product_service.dto.response.ProductResponse;
import com.vorder.product_service.entity.Product;
import com.vorder.product_service.mapper.InventoryMapper;
import com.vorder.product_service.mapper.ProductMapper;
import com.vorder.product_service.repository.ProductRepository;
import com.vorder.product_service.repository.http.InventoryClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductMapper productMapper;
    InventoryMapper inventoryMapper;
    ProductRepository productRepository;
    InventoryClient inventoryClient;


    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .images(productRequest.getImages())
                .build();

        Product savedProduct = productRepository.save(product);

        var inventoryRequest = inventoryMapper.toCreateInventoryRequest(productRequest);
        inventoryRequest.setProductId(savedProduct.getId());

        log.info("req {}", inventoryRequest);


        inventoryClient.create(inventoryRequest);


        return productMapper.productToProductResponse(savedProduct);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::productToProductResponse).toList();
    }
}
