package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    protected ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable long id){
        return restaurantService.getRestaurantById(id);
    }

//    @PostMapping
//    protected ResponseEntity<RestaurantResponse> addRestaurant(){}
}
