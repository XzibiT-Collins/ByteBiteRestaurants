package com.example.restaurant_service.service.serviceInterfaces;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface RestaurantService {
    ResponseEntity<Restaurant> addRestaurant(RestaurantRequest restaurantRequest);
    ResponseEntity<RestaurantResponse> getRestaurantById(long id);
    ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(int pageNumber,String sortField);
    ResponseEntity<RestaurantResponse> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest, long id);
    ResponseEntity<String> deleteRestaurant(long id);

}
