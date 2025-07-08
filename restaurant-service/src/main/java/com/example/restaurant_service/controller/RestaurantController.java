package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
import com.example.restaurant_service.utils.RestaurantSort;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    protected ResponseEntity<ApiResponseDto<RestaurantResponse>> getRestaurantById(@PathVariable long id){
        return restaurantService.getRestaurantById(id);
    }

    @PreAuthorize( "hasRole('RESTAURANT_OWNER')")
    @PostMapping
    protected ResponseEntity<ApiResponseDto<RestaurantResponse>> addRestaurant(@RequestBody RestaurantRequest restaurantRequest){
        return restaurantService.addRestaurant(restaurantRequest);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<Page<RestaurantResponse>>> getAllRestaurants(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                                      @RequestParam(required = false, defaultValue = "SORT_BY_NAME") RestaurantSort sortField){
        return restaurantService.getAllRestaurants(pageNumber,sortField.getField());
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isRestaurantOwner(#restaurantId,authentication.getPrincipal())")
    @PatchMapping("/{restaurantId}")
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> updateRestaurant(@RequestBody RestaurantUpdateRequest restaurantUpdateRequest, @PathVariable long restaurantId){
        return restaurantService.updateRestaurant(restaurantUpdateRequest,restaurantId);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isRestaurantOwner(#restaurantId,authentication.getPrincipal())")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ApiResponseDto<String>> deleteRestaurant(@PathVariable long restaurantId){
        return restaurantService.deleteRestaurant(restaurantId);
    }
}
