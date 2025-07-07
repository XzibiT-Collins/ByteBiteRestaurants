package com.example.order_service.service;

import com.example.order_service.dto.ApiResponseDto;
import com.example.order_service.dto.requestDto.OrderRequest;
import com.example.order_service.dto.responseDto.OrderResponse;
import com.example.order_service.globalExceptionHandler.ErrorResponse;
import com.example.order_service.globalExceptionHandler.customExceptions.InvalidOrderException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.utils.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String,OrderRequest> kafkaTemplate;


    public OrderServiceImpl(OrderRepository orderRepository, KafkaTemplate<String, OrderRequest> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public ResponseEntity<ApiResponseDto<OrderResponse>> createOrder(OrderRequest orderRequest) {
        if(orderRequest == null){
            throw new InvalidOrderException("Order request is null");
        }
        //TODO: send message to restaurant service to prepare order
        kafkaTemplate.send("order-request",orderRequest);

        return ResponseEntity.ok(ApiResponseDto.success(OrderMapper.toOrderResponse(orderRepository.save(OrderMapper.toOrder(orderRequest))), HttpStatus.CREATED.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<OrderResponse>> getOrderById(long customerId, long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new InvalidOrderException("Order not found"));
        return ResponseEntity.ok(ApiResponseDto.success(OrderMapper.toOrderResponse(order), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<OrderResponse>>> getAllOrders(long customerId, int pageNumber, String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);
        return ResponseEntity.ok(ApiResponseDto.success(orderRepository.findAll(pageable).map(OrderMapper::toOrderResponse),HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> cancelOrder(long customerId, long id) {
        Order order = findOrder(id);
        if(order.getStatus().equals(OrderStatus.PENDING)){
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return ResponseEntity.ok(ApiResponseDto.success("Order cancelled successfully",HttpStatus.OK.value()));
        }

        return ResponseEntity.badRequest().body(ApiResponseDto.error(new ErrorResponse("Order cannot be cancelled",HttpStatus.BAD_REQUEST.value()),null)) ;
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> deleteOrder(long customerId, long id) {
        Order order = findOrder(id);
        if(order.getStatus().equals(OrderStatus.PENDING)){
            orderRepository.delete(order);
            return ResponseEntity.ok(ApiResponseDto.success("Order deleted successfully",HttpStatus.OK.value()));
        }
        return ResponseEntity.badRequest().body(ApiResponseDto.error(new ErrorResponse("Order cannot be deleted",HttpStatus.BAD_REQUEST.value()),null)) ;
    }


    private Order findOrder(long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new InvalidOrderException("Order not found"));
        if(order.getStatus().equals(OrderStatus.COMPLETED)){
            throw new InvalidOrderException("Order is already completed");
        }
        if(order.getStatus().equals(OrderStatus.CANCELLED)){
            throw new InvalidOrderException("Order is already cancelled");
        }
        return order;
    }
}
