package com.example.order_service.mapper;

import com.example.order_service.dto.requestDto.OrderRequest;
import com.example.order_service.dto.responseDto.OrderResponse;
import com.example.order_service.model.Order;
import com.example.order_service.utils.OrderStatus;

import java.time.Instant;

public class OrderMapper {
    public static OrderResponse toOrderResponse(Order order) {
        return OrderResponse
                .builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .menuItemId(order.getMenuItemId())
                .quantity(order.getQuantity())
                .status(order.getStatus().toString())
                .totalPrice(order.getTotalPrice())
                .orderTime(order.getOrderTime())
                .build();
    }

    public static Order toOrder(OrderRequest orderRequest){
        return Order
                .builder()
                .customerId(orderRequest.customerId())
                .menuItemId(orderRequest.menuItemId())
                .restaurantId(orderRequest.restaurantId())
                .quantity(orderRequest.quantity())
                .status(OrderStatus.PENDING)
                .totalPrice(orderRequest.totalPrice())
                .orderTime(Instant.now())
                .build();
    }
}
