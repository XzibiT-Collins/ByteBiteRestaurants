package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.service.serviceInterfaces.FoodMenuService;
import com.example.restaurant_service.utils.FoodMenuSort;
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
public class FoodMenuController {
    private final FoodMenuService foodMenuService;

    public FoodMenuController(FoodMenuService foodMenuService) {
        this.foodMenuService = foodMenuService;
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<ApiResponseDto<Page<FoodMenuResponse>>> getRestaurantMenu(@PathVariable long restaurantId,
                                                                                    @Valid @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                                                    @Valid @RequestParam(required = false, defaultValue = "SORT_BY_NAME") FoodMenuSort sortField,
                                                                                    Authentication authenticatedUser){
        log.info("Fetching all food menus for restaurant with id: {} and sortField: {} for user with id: {}",restaurantId,sortField.getField(), authenticatedUser.getPrincipal());
        return foodMenuService.getAllFoodMenus(restaurantId,pageNumber,sortField.getField());
    }

    @GetMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> getFoodMenuById(@PathVariable long restaurantId,
                                                                            @PathVariable long menuId,
                                                                            Authentication authenticatedUser){
        log.info("Fetching food menu with id: {} for user with id: {}",menuId,authenticatedUser.getPrincipal());
        return foodMenuService.getFoodMenuById(menuId,restaurantId);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> addFoodMenu(@PathVariable long restaurantId,
                                                                        @Valid @RequestBody FoodMenuRequest foodMenuRequest,
                                                                        Authentication authenticatedUser){
        log.info("Adding food menu with name: {} for restaurant with id: {} for user with id: {}",foodMenuRequest.name(),restaurantId, authenticatedUser.getPrincipal());
        return foodMenuService.addFoodMenu(restaurantId,foodMenuRequest);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isFoodMenuOwner(#menuId,#authentication)")
    @PatchMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> updateFoodMenu(@Valid @RequestBody FoodMenuUpdateRequest foodMenuUpdateRequest,
                                                                           @PathVariable long restaurantId,
                                                                           @PathVariable long menuId,
                                                                           Authentication authenticatedUser){
        log.info("Updating food menu item with if: {} for restaurant with id: {}, and user with id: {}",menuId,restaurantId,authenticatedUser.getPrincipal());
        return foodMenuService.updateFoodMenu(foodMenuUpdateRequest,restaurantId,menuId);
    }

    @PreAuthorize("hasRole('RESTAURANT_OWNER') and @resourceOwner.isFoodMenuOwner(#menuId,#authentication)")
    @DeleteMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<ApiResponseDto<String>> deleteFoodMenu(@PathVariable long restaurantId,
                                                                 @PathVariable long menuId, Authentication authenticatedUser){
        log.info("Deleting food menu item with id: {} for restaurant with id: {}, and user with id: {}",menuId,restaurantId,authenticatedUser.getPrincipal());
        return foodMenuService.deleteFoodMenu(restaurantId,menuId);
    }
}