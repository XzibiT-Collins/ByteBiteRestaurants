package com.example.order_service.controller;

import com.example.order_service.dto.requestDto.OrderRequest;
import com.example.order_service.dto.responseDto.OrderResponse;
import com.example.order_service.service.OrderService;
import com.example.order_service.utils.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('CUSTOMER')")
@RequestMapping("/api/v1/order/")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);
    }

    @PreAuthorize("@resourceOwner.isOrderOwner(#orderId,authentication.getPrincipal())")
    @GetMapping("{customerId}/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable long customerId,
                                                      @PathVariable long orderId){
        return orderService.getOrderById(customerId,orderId);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(@PathVariable long customerId,
                                                           @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                           @RequestParam(required = false, defaultValue = "PENDING") OrderStatus sortField){
        return orderService.getAllOrders(customerId,pageNumber,sortField.toString());
    }

    @PreAuthorize("@resourceOwner.isOrderOwner(#orderId,authentication.getPrincipal())")
    @PatchMapping("{customerId}/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable long customerId,
                                              @PathVariable long orderId){
        return orderService.cancelOrder(customerId,orderId);
    }

    @PreAuthorize("@resourceOwner.isOrderOwner(#orderId,authentication.getPrincipal())")
    @DeleteMapping("{customerId}/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable long customerId,
                                              @PathVariable long orderId){
        return orderService.deleteOrder(customerId,orderId);
    }
}
