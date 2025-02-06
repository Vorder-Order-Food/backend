package com.vorder.order_service.controller;

import com.vorder.order_service.dto.OrderDto;
import com.vorder.order_service.dto.request.OrderRequest;
import com.vorder.order_service.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class InternalOrderController {

    OrderService orderService;


    @PostMapping("/vnpay")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest orderRequest) {

        OrderDto order =  orderService.createOrder(orderRequest);


        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
