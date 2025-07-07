package com.example.order_service.service;

import com.example.order_service.dto.ApiResponseDto;
import com.example.order_service.dto.requestDto.OrderRequest;
import com.example.order_service.dto.responseDto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    ResponseEntity<ApiResponseDto<OrderResponse>> createOrder(OrderRequest orderRequest);
    ResponseEntity<ApiResponseDto<OrderResponse>> getOrderById(long customerId, long id);
    ResponseEntity<ApiResponseDto<Page<OrderResponse>>> getAllOrders(long customerId, int pageNumber, String sortField);
    ResponseEntity<ApiResponseDto<String>> cancelOrder(long customerId, long id);
    ResponseEntity<ApiResponseDto<String>> deleteOrder(long customerId, long id);
}
