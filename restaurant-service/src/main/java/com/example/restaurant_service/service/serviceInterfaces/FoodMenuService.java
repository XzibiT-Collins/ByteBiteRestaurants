package com.example.restaurant_service.service.serviceInterfaces;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface FoodMenuService {
    ResponseEntity<ApiResponseDto<FoodMenuResponse>> addFoodMenu(long restaurantId, FoodMenuRequest foodMenuRequest);
    ResponseEntity<ApiResponseDto<FoodMenuResponse>> getFoodMenuById(long id,long restaurantId);
    ResponseEntity<ApiResponseDto<Page<FoodMenuResponse>>> getAllFoodMenus(long restaurantId,int pageNumber, String sortField);
    ResponseEntity<ApiResponseDto<FoodMenuResponse>> updateFoodMenu(FoodMenuUpdateRequest foodMenuUpdateRequest, long restaurantId, long menuId);
    ResponseEntity<ApiResponseDto<String>> deleteFoodMenu(long restaurantId,long id);
}
