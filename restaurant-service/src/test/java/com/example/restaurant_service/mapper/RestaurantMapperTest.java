package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantMapperTest {

    @Test
    public void shouldMapRestaurantToRestaurantResponse() {
        Restaurant restaurant = Restaurant
                .builder()
                .id(1L)
                .name("name")
                .location("location")
                .email("email")
                .phoneNumber("phoneNumber")
                .ownerId(1)
                .build();

        RestaurantResponse restaurantResponse = RestaurantMapper
                .toRestaurantResponse(restaurant);

        assertEquals(restaurant.getId(), restaurantResponse.id());
        assertEquals(restaurant.getName(), restaurantResponse.name());
        assertEquals(restaurant.getLocation(), restaurantResponse.location());
        assertEquals(restaurant.getEmail(), restaurantResponse.email());
        assertEquals(restaurant.getPhoneNumber(), restaurantResponse.phoneNumber());
        assertEquals(restaurant.getOwnerId(), restaurantResponse.owner());
    }

    @Test
    public void shouldMapRestaurantRequestToRestaurant() {
        RestaurantRequest request = new RestaurantRequest("name",
                "location",
                "phoneNumber",
                "123@gmail.com",
                1L);

        Restaurant restaurant = RestaurantMapper.toRestaurant(request);

        assertEquals(request.name(), restaurant.getName());
        assertEquals(request.location(), restaurant.getLocation());
        assertEquals(request.phoneNumber(), restaurant.getPhoneNumber());
        assertEquals(request.email(), restaurant.getEmail());
        assertEquals(request.ownerId(), restaurant.getOwnerId());
    }

    @Test
    public void should_throw_nullPointerException_when_restaurantRequest_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> RestaurantMapper.toRestaurant(null));
        assertEquals("Restaurant Request cannot be Null",exp.getMessage() );
    }
}