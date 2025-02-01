package com.vorder.cart_service.repository.http;

import com.vorder.cart_service.configuration.AuthenticationRequestInterceptor;
import com.vorder.cart_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${app.services.product}",
        configuration = { AuthenticationRequestInterceptor.class }
)
public interface ProductClient {
    @GetMapping("/product/{id}")
    ProductDto getFoodById(@PathVariable("id") String id);

}
