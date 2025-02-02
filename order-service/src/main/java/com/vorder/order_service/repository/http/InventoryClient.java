package com.vorder.order_service.repository.http;

import com.vorder.order_service.configuration.AuthenticationRequestInterceptor;
import com.vorder.order_service.dto.request.IsInStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", url = "${app.services.inventory}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface InventoryClient {

    @GetMapping("/inventory/isInStock")
    boolean isInStock(@RequestParam String productId, @RequestParam int quantity);

}
