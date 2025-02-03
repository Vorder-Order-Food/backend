package com.vorder.product_service.mapper;

import com.vorder.product_service.dto.request.CreateInventoryRequest;
import com.vorder.product_service.dto.request.ProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    CreateInventoryRequest toCreateInventoryRequest(ProductRequest request);
}
