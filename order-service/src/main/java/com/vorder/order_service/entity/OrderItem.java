package com.vorder.order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Reference to the product by its ID
    private String productId;

    // Copy relevant product details for convenience
    private String productName;
    private Long productPrice;

    // Quantity of this product in the order
    private int quantity;

    // Total price for this item (productPrice * quantity)
    private Long totalPrice;
}
