package com.example.restaurant_service.service.serviceInterfaces;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface RestaurantService {
    ResponseEntity<ApiResponseDto<RestaurantResponse>> addRestaurant(RestaurantRequest restaurantRequest);
    ResponseEntity<ApiResponseDto<RestaurantResponse>> getRestaurantById(long id);
    ResponseEntity<ApiResponseDto<Page<RestaurantResponse>>> getAllRestaurants(int pageNumber,String sortField);
    ResponseEntity<ApiResponseDto<RestaurantResponse>> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest, long id);
    ResponseEntity<ApiResponseDto<String>> deleteRestaurant(long id);

}
