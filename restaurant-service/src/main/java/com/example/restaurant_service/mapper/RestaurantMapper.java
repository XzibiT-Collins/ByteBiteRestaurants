package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.model.Restaurant;

public class RestaurantMapper {
    public static Restaurant toRestaurant(RestaurantRequest restaurantRequest){
        if(restaurantRequest == null) throw new NullPointerException("Restaurant Request cannot be Null");

        return Restaurant
                .builder()
                .name(restaurantRequest.name())
                .location(restaurantRequest.location())
                .email(restaurantRequest.email())
                .phoneNumber(restaurantRequest.phoneNumber())
                .ownerId(restaurantRequest.ownerId())
                .build();
    }

    public static RestaurantResponse toRestaurantResponse(Restaurant restaurant){
        return RestaurantResponse
                .builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .location(restaurant.getLocation())
                .phoneNumber(restaurant.getPhoneNumber())
                .email(restaurant.getEmail())
                .owner(restaurant.getOwnerId())
                .build();
    }
}
