package com.vorder.order_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vorder.order_service.entity.OrderItem;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
     String userId;

     Long totalAmount;
     String orderStatus;
     Instant createdAt;

     int totalItem;
     Long totalPrice;
}
