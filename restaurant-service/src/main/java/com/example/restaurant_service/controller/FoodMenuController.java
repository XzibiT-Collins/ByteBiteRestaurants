package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.service.serviceInterfaces.FoodMenuService;
import com.example.restaurant_service.utils.FoodMenuSort;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/restaurant")
public class FoodMenuController {
    private final FoodMenuService foodMenuService;

    public FoodMenuController(FoodMenuService foodMenuService) {
        this.foodMenuService = foodMenuService;
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<Page<FoodMenuResponse>> getRestaurantMenu(@PathVariable long restaurantId,
                                                                    @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                                                    @RequestParam(required = false, defaultValue = "SORT_BY_NAME") FoodMenuSort sortField){
        return foodMenuService.getAllFoodMenus(restaurantId,pageNumber,sortField.toString());
    }

    @GetMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<FoodMenuResponse> getFoodMenuById(@PathVariable long restaurantId,
                                                            @PathVariable long menuId){
        return foodMenuService.getFoodMenuById(menuId,restaurantId);
    }

    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<FoodMenuResponse> addFoodMenu(@PathVariable long restaurantId,
                                                        @RequestBody com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest foodMenuRequest){
        return foodMenuService.addFoodMenu(restaurantId,foodMenuRequest);
    }

    @PatchMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<FoodMenuResponse> updateFoodMenu(@RequestBody FoodMenuUpdateRequest foodMenuUpdateRequest,
                                                            @PathVariable long restaurantId,
                                                            @PathVariable long menuId){
        return foodMenuService.updateFoodMenu(foodMenuUpdateRequest,restaurantId,menuId);
    }

    @DeleteMapping("/{restaurantId}/menu/{menuId}")
    public ResponseEntity<String> deleteFoodMenu(@PathVariable long restaurantId,
                                                  @PathVariable long menuId){
        return foodMenuService.deleteFoodMenu(restaurantId,menuId);
    }
}
