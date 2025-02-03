package com.vorder.order_service.service;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.vorder.event.NotificationEvent;
import com.vorder.order_service.dto.CartDto;
import com.vorder.order_service.dto.CartItemDto;
import com.vorder.order_service.dto.UserDto;
import com.vorder.order_service.dto.request.InventoryUpdateRequest;
import com.vorder.order_service.dto.request.OrderRequest;
import com.vorder.order_service.entity.Order;
import com.vorder.order_service.entity.OrderItem;
import com.vorder.order_service.exception.AppException;
import com.vorder.order_service.exception.ErrorCode;
import com.vorder.order_service.repository.OrderItemRepository;
import com.vorder.order_service.repository.OrderRepository;
import com.vorder.order_service.repository.http.CartClient;
import com.vorder.order_service.repository.http.InventoryClient;
import com.vorder.order_service.repository.http.UserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    CartClient cartClient;
    OrderItemRepository orderItemRepository;
    UserClient userClient ;
    KafkaTemplate<String, Object> kafkaTemplate;
    InventoryClient inventoryClient;


    public Order createOrder(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CartDto cartDto = cartClient.getCartByUserId(authentication.getName());

        Order order = Order.builder()
                .userId(authentication.getName())
                .createdAt(Instant.now())
                .orderStatus("PENDING")
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        List<InventoryUpdateRequest> requests = new ArrayList<>();




        for (CartItemDto cartItem : cartDto.getItems()) {


            var isInStock = inventoryClient.isInStock(cartItem.getProductId(), cartItem.getQuantity());

            if(isInStock){
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getProductName());
                orderItem.setProductPrice(cartItem.getProductPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setTotalPrice(cartItem.getTotalPrice());

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);

                requests.add(new InventoryUpdateRequest(cartItem.getProductId(), cartItem.getQuantity()));

            } else {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }


        }

        Long totalPrice = orderItems.stream()
                .mapToLong(OrderItem::getTotalPrice)
                .sum();

        order.setItems(orderItems);
        order.setTotalItem(orderItems.size());
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        inventoryClient.updateStock(requests);

        UserDto user = userClient.getUser(authentication.getName());

        log.info("user: {}", user);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(user.getEmail())
                .subject("Order Confirmation")
                .body("order sucess")
                .build();

        kafkaTemplate.send("notification-send", notificationEvent);

        return savedOrder;
    }

    public String getIdFromJwt(String jwt) throws Exception {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        } else {
            throw new Exception("Invalid JWT token format");
        }

        JWSObject jwsObject = JWSObject.parse(jwt);

        Payload payload = jwsObject.getPayload();
        Map<String, Object> claimsMap = payload.toJSONObject();

        String userId = (String) claimsMap.get("sub");
        return userId;
    }

    public List<Order> getUserOrders(String userId)  {
        return orderRepository.findOrdersByUserId(userId);
    }

    public List<Order> getRestaurantsOrder( String orderStatus) {

        List<Order> orders = orderRepository.findAll();

        if(orderStatus != null){
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }

    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY") || orderStatus.equals("DELIVERED") || orderStatus.equals("COMPLETED")
                || orderStatus.equals("PENDING")){
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }

        throw new Exception("Order status is not cvalid");
    }

    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> opt = orderRepository.findById(orderId);

        if(opt.isEmpty()){
            throw new Exception("Order not found");
        }
        return opt.get();
    }

}
