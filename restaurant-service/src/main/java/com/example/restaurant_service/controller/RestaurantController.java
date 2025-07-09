package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
import com.example.restaurant_service.utils.RestaurantSort;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    protected ResponseEntity<ApiResponseDto<RestaurantResponse>> getRestaurantById(@PathVariable long id,
                                                                                   Authentication authenticatedUser){
        log.info("Fetching restaurant with id: {} for user with id: {}",id,authenticatedUser.getPrincipal());
        return restaurantService.getRestaurantById(id);
    }

    @PreAuthorize( "hasRole('RESTAURANT_OWNER')")
    @PostMapping
    protected ResponseEntity<ApiResponseDto<RestaurantResponse>> addRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest,
                                                                               Authentication authenticatedUser){
        log.info("Adding restaurant with name: {} for user with id: {}",restaurantRequest.name(),authenticatedUser.getPrincipal());
        return restaurantService.addRestaurant(restaurantRequest);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<Page<RestaurantResponse>>> getAllRestaurants(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                                                      @RequestParam(required = false, defaultValue = "SORT_BY_NAME") RestaurantSort sortField,
                                                                                      Authentication authenticatedUser){
        log.info("Fetching all restaurants with pageNumber: {} and sortField: {} for user with id: {}",pageNumber,sortField.getField(), authenticatedUser.getPrincipal());
        return restaurantService.getAllRestaurants(pageNumber,sortField.getField());
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isRestaurantOwner(#restaurantId,authentication.getPrincipal())")
    @PatchMapping("/{restaurantId}")
    public ResponseEntity<ApiResponseDto<RestaurantResponse>> updateRestaurant(@Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest,
                                                                               @PathVariable long restaurantId,
                                                                               Authentication authenticatedUser){
        log.info("Updating restaurant with id: {} for user with id: {}",restaurantId,authenticatedUser.getPrincipal());
        return restaurantService.updateRestaurant(restaurantUpdateRequest,restaurantId);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isRestaurantOwner(#restaurantId,authentication.getPrincipal())")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ApiResponseDto<String>> deleteRestaurant(@PathVariable long restaurantId, Authentication authenticatedUser){
        log.info("Deleting restaurant with id: {} for user with id: {}",restaurantId, authenticatedUser.getPrincipal());
        return restaurantService.deleteRestaurant(restaurantId);
    }
}
