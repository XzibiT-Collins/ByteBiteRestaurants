package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.model.FoodMenu;

public class FoodMenuMapper {
    public static FoodMenuResponse toFoodMenuResponse(FoodMenu foodMenu) {
        return FoodMenuResponse
                .builder()
                .id(foodMenu.getId())
                .name(foodMenu.getName())
                .description(foodMenu.getDescription())
                .price(foodMenu.getPrice())
                .build();
    }

    public static FoodMenu toFoodMenu(FoodMenuRequest foodMenuRequest) {
        if(foodMenuRequest == null) throw new NullPointerException("Food Menu Request cannot be Null");
        return FoodMenu
                .builder()
                .name(foodMenuRequest.name())
                .description(foodMenuRequest.description())
                .price(foodMenuRequest.price())
                .build();
    }
}
