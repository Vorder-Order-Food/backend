package com.vorder.payment_service.repository.http;

import com.vorder.payment_service.configuration.AuthenticationRequestInterceptor;
import com.vorder.payment_service.dto.OrderDto;
import com.vorder.payment_service.dto.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", url = "${app.services.order}",  configuration = { AuthenticationRequestInterceptor.class })
public interface OrderClient {
    @GetMapping("/order/vnpay")
    OrderDto createOrder(@RequestBody OrderRequest orderRequest);
}
