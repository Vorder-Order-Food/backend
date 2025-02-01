package com.vorder.product_service.mapper;

import com.vorder.product_service.dto.ProductDto;
import com.vorder.product_service.dto.response.ProductResponse;
import com.vorder.product_service.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse productToProductResponse(Product product);

    ProductDto productToProductDto(Product product);
}
