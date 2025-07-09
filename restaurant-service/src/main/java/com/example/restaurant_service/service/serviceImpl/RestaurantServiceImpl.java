package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.UserResponseDto;
import com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto.NotificationPayload;
import com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto.OrderRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.feignClient.UserClient;
import com.example.restaurant_service.globalExceptionHandler.customExceptions.RestaurantException;
import com.example.restaurant_service.mapper.RestaurantMapper;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final KafkaTemplate<String, NotificationPayload> kafkaTemplate;
    private final UserClient userClient;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 KafkaTemplate<String, NotificationPayload> kafkaTemplate,
                                 UserClient userClient) {
        this.restaurantRepository = restaurantRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.userClient = userClient;
    }


    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> addRestaurant(RestaurantRequest restaurantRequest) {
        if(restaurantRequest != null){
            Restaurant restaurant = restaurantRepository.save(RestaurantMapper.toRestaurant(restaurantRequest));
            log.info("Restaurant Saved Successfully with id: {}",restaurant.getId());
            return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurant), HttpStatus.CREATED.value()));
        }else{
            log.info("Unable to save restaurant with null request.");
            throw new RestaurantException("Restaurant Request is Null");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> getRestaurantById(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantException("Restaurant Not Found with id: " + id));
        log.info("Restaurant Found with id: {} ",id);
        return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurant), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<RestaurantResponse>>> getAllRestaurants(int pageNumber,String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);
        log.info("Fetching all restaurants with pageNumber: {} and sortField: {} ",pageNumber, sortField);
        return ResponseEntity.ok(ApiResponseDto.success(restaurantRepository.findAll(pageable).map(RestaurantMapper::toRestaurantResponse), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest, long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantException("Restaurant Not Found with id: " + id));

        if(restaurantUpdateRequest.name() != null) restaurant.setName(restaurantUpdateRequest.name());
        if(restaurantUpdateRequest.location() != null) restaurant.setLocation(restaurantUpdateRequest.location());
        if(restaurantUpdateRequest.email() != null) restaurant.setEmail(restaurantUpdateRequest.email());
        if(restaurantUpdateRequest.phoneNumber() != null) restaurant.setPhoneNumber(restaurantUpdateRequest.phoneNumber());
        log.info("Restaurant Updated Successfully with id: {}",id);
        return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurantRepository.save(restaurant)), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> deleteRestaurant(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurant Not Found with id: " + id));
        restaurantRepository.delete(restaurant);
        log.info("Restaurant Deleted Successfully with id: {}",id);
        return ResponseEntity.ok(ApiResponseDto.success("Restaurant Deleted Successfully", HttpStatus.OK.value()));
    }

    @KafkaListener(topics = "order-request", groupId = "restaurant-group",containerFactory = "kafkaListenerContainerFactory")
    public void prepareOrder(OrderRequest orderRequest){
        log.info("Preparing Order for: {} ",orderRequest.customerId());
        try{
            Thread.sleep(10000);
        }catch (InterruptedException e){
            throw new RestaurantException("Failed to prepare order.");
        }
        log.info("Making a request to fetch user details for: {} ",orderRequest.customerId());
        String email = "";

        try{
            ResponseEntity<ApiResponseDto<UserResponseDto>> user = userClient.getUserById(orderRequest.customerId());
            log.info("User Details fetched for: {} with email: {}",orderRequest.customerId(), user.getBody().getData().email());
            if(user.getBody() != null){
                email = user.getBody().getData().email();
            }
        }catch (Exception e){
            log.error("Failed to fetch user details for: {} with error: {}",orderRequest.customerId(), e.getMessage());
        }

        NotificationPayload notificationPayload = new NotificationPayload(email,"Your order have been prepared and is out for delivery.");
        kafkaTemplate.send("order-completed",notificationPayload);
        log.info("Order Prepared Successfully for: {} with email: {}",orderRequest.customerId(), email);
    }
}
