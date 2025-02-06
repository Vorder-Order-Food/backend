package com.vorder.order_service.controller;

import com.vorder.order_service.dto.ApiResponse;
import com.vorder.order_service.dto.OrderDto;
import com.vorder.order_service.dto.request.OrderRequest;
import com.vorder.order_service.dto.response.OrderResponse;
import com.vorder.order_service.entity.Order;
import com.vorder.order_service.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;

    @PostMapping("/create")
    ApiResponse<OrderDto> createOrder(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.<OrderDto>builder()
                .result(orderService.createOrder(orderRequest))
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String jwt) throws Exception {

        String userId = orderService.getIdFromJwt(jwt);
        List<Order> orders = orderService.getUserOrders(userId);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
