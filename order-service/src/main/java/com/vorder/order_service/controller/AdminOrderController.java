package com.vorder.order_service.controller;

import com.vorder.order_service.dto.UserDto;
import com.vorder.order_service.dto.response.AdminOrderResponse;
import com.vorder.order_service.dto.response.OrderResponse;
import com.vorder.order_service.entity.Order;
import com.vorder.order_service.mapper.OrderMapper;
import com.vorder.order_service.repository.http.UserClient;
import com.vorder.order_service.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {

     OrderService orderService;
     UserClient userClient;
     OrderMapper orderMapper;

    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponse>> getOrderHistory(
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("Authorization") String jwt) throws Exception {

        List<Order> orders = orderService.getRestaurantsOrder(orderStatus);

        List<AdminOrderResponse> orderResponses = orders.stream()
                .map(order -> orderMapper.toResponse(order, userClient.getUser(order.getUserId())))
                .toList();

        return ResponseEntity.ok(orderResponses);
    }


    @PutMapping("/order/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                   @PathVariable String orderStatus,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {

        Order order = orderService.updateOrder(id, orderStatus);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}

