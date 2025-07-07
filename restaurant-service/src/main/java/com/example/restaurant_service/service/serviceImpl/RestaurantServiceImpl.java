package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto.NotificationPayload;
import com.example.restaurant_service.dto.restaurantDto.kafkaMessageDto.OrderRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.globalExceptionHandler.customExceptions.RestaurantException;
import com.example.restaurant_service.mapper.RestaurantMapper;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
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
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final KafkaTemplate<String, NotificationPayload> kafkaTemplate;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, KafkaTemplate<String, NotificationPayload> kafkaTemplate) {
        this.restaurantRepository = restaurantRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> addRestaurant(RestaurantRequest restaurantRequest) {
        if(restaurantRequest != null){
            Restaurant restaurant = restaurantRepository.save(RestaurantMapper.toRestaurant(restaurantRequest));
            return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurant), HttpStatus.CREATED.value()));
        }else{
            throw new RestaurantException("Restaurant Request is Null");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> getRestaurantById(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantException("Restaurant Not Found with id: " + id));

        return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurant), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<RestaurantResponse>>> getAllRestaurants(int pageNumber,String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        return ResponseEntity.ok(ApiResponseDto.success(restaurantRepository.findAll(pageable).map(RestaurantMapper::toRestaurantResponse), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest, long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RestaurantException("Restaurant Not Found with id: " + id));

        if(restaurantUpdateRequest.name() != null) restaurant.setName(restaurantUpdateRequest.name());
        if(restaurantUpdateRequest.location() != null) restaurant.setLocation(restaurantUpdateRequest.location());
        if(restaurantUpdateRequest.email() != null) restaurant.setEmail(restaurantUpdateRequest.email());
        if(restaurantUpdateRequest.phoneNumber() != null) restaurant.setPhoneNumber(restaurantUpdateRequest.phoneNumber());

        return ResponseEntity.ok(ApiResponseDto.success(RestaurantMapper.toRestaurantResponse(restaurantRepository.save(restaurant)), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> deleteRestaurant(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurant Not Found with id: " + id));
        restaurantRepository.delete(restaurant);
        return ResponseEntity.ok(ApiResponseDto.success("Restaurant Deleted Successfully", HttpStatus.OK.value()));
    }

    @KafkaListener(topics = "order-request", groupId = "restaurant-group",containerFactory = "kafkaListenerContainerFactory")
    public void prepareOrder(OrderRequest orderRequest){
        System.out.println("Preparing Order for: "+ orderRequest);
        try{
            Thread.sleep(10000);
        }catch (InterruptedException e){
            throw new RestaurantException("Failed to prepare order.");
        }
        //TODO fetch user email from user-service
        NotificationPayload notificationPayload = new NotificationPayload("huvisoncollins@gmail.com","Your order have been prepared.");
        kafkaTemplate.send("order-completed",notificationPayload);
    }
}
