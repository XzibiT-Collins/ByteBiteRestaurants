package com.example.order_service.service;

import com.example.order_service.dto.requestDto.OrderRequest;
import com.example.order_service.dto.responseDto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest);
    ResponseEntity<OrderResponse> getOrderById(long customerId, long id);
    ResponseEntity<Page<OrderResponse>> getAllOrders(long customerId, int pageNumber, String sortField);
    ResponseEntity<String> cancelOrder(long customerId, long id);
    ResponseEntity<String> deleteOrder(long customerId, long id);
}
