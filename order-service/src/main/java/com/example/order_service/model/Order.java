package com.example.order_service.model;

import com.example.order_service.utils.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long menuItemId;
    private long restaurantId;
    private long customerId;
    private int quantity = 1;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    private Instant orderTime = Instant.now();
}
