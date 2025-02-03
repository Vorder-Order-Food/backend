package com.vorder.product_service.repository.http;

import com.vorder.product_service.configuration.AuthenticationRequestInterceptor;
import com.vorder.product_service.dto.ApiResponse;
import com.vorder.product_service.dto.request.CreateInventoryRequest;
import com.vorder.product_service.dto.response.CreateInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "${app.services.inventory}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface InventoryClient {
    @PostMapping("/inventory/create")
    ApiResponse<CreateInventoryResponse> create(@RequestBody CreateInventoryRequest createInventoryRequest);
}
