package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.model.FoodMenu;
import com.example.restaurant_service.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodMenuMapperTest {
    private Restaurant restaurant;
    @BeforeEach
    void beforeEach() {
        restaurant = Restaurant
                .builder()
                .id(1L)
                .name("Burger")
                .location("Burger location")
                .email("email@gmail.com")
                .phoneNumber("1234567890")
                .ownerId(1L)
                .build();
    }

    @Test
    public void shouldMapFoodMenuToFoodMenuResponse() {
        FoodMenu menu = FoodMenu
                .builder()
                .id(1L)
                .name("Burger")
                .description("Burger description")
                .price(10.0)
                .restaurant(restaurant)
                .build();

        FoodMenuResponse response = FoodMenuMapper.toFoodMenuResponse(menu);

        assertEquals(menu.getId(), response.id());
        assertEquals(menu.getName(), response.name());
        assertEquals(menu.getDescription(), response.description());
        assertEquals(menu.getPrice(), response.price());

    }

    @Test
    public void shouldMapFoodMenuRequestToFoodMenu() {
        FoodMenuRequest request = new FoodMenuRequest("Burger",
                "Burger description",
                10.0);

        FoodMenu menu = FoodMenuMapper.toFoodMenu(request);

        assertEquals(request.name(), menu.getName());
        assertEquals(request.price(), menu.getPrice());
        assertEquals(request.description(), menu.getDescription());
    }

    @Test
    public void should_throw_nullPointerException_when_foodMenuRequest_is_null() {
        var exp = assertThrows(NullPointerException.class, () -> FoodMenuMapper.toFoodMenu(null));
        assertEquals("Food Menu Request cannot be Null",exp.getMessage() );
    }
}