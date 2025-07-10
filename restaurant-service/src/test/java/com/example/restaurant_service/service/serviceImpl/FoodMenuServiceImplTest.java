package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.globalExceptionHandler.customExceptions.RestaurantException;
import com.example.restaurant_service.model.FoodMenu;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.FoodMenuRepository;
import com.example.restaurant_service.repository.RestaurantRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodMenuServiceImplTest {

    @Mock
    private FoodMenuRepository foodMenuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private FoodMenuServiceImpl foodMenuService;

    private Restaurant restaurant;
    private FoodMenu foodMenu;
    private FoodMenuRequest foodMenuRequest;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Resto");

        foodMenu = new FoodMenu();
        foodMenu.setId(10L);
        foodMenu.setName("Jollof Rice");
        foodMenu.setDescription("Spicy Ghanaian Jollof");
        foodMenu.setPrice(25.50);
        foodMenu.setRestaurant(restaurant);

        foodMenuRequest = new FoodMenuRequest(
                "Jollof Rice",
                "Spicy Ghanaian Jollof",
                25.50
        );
    }

    // --- addFoodMenu ---
    @Test
    void addFoodMenu_shouldSaveAndReturnResponse() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.save(any())).thenReturn(foodMenu);

        var response = foodMenuService.addFoodMenu(1L, foodMenuRequest);

        assertEquals("Jollof Rice", response.getBody().getData().name());
        assertEquals(25.50, response.getBody().getData().price());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
    }

    @Test
    void addFoodMenu_shouldThrowIfRequestIsNull() {
        assertThrows(RestaurantException.class, () -> foodMenuService.addFoodMenu(1L, null));
    }

    @Test
    void addFoodMenu_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RestaurantException.class, () -> foodMenuService.addFoodMenu(1L, foodMenuRequest));
    }

    // --- getFoodMenuById ---
    @Test
    void getFoodMenuById_shouldReturnMenuItem() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.of(foodMenu));

        var response = foodMenuService.getFoodMenuById(10L, 1L);

        assertEquals("Jollof Rice", response.getBody().getData().name());
        assertEquals(25.50, response.getBody().getData().price());
    }

    @Test
    void getFoodMenuById_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RestaurantException.class, () -> foodMenuService.getFoodMenuById(10L, 1L));
    }

    @Test
    void getFoodMenuById_shouldThrowIfMenuNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.empty());
        assertThrows(RestaurantException.class, () -> foodMenuService.getFoodMenuById(10L, 1L));
    }

    // --- getAllFoodMenus ---
    @Test
    void getAllFoodMenus_shouldReturnPagedMenus() {
        Page<FoodMenu> page = new PageImpl<>(List.of(foodMenu));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findAllByRestaurant(eq(restaurant), any(Pageable.class))).thenReturn(page);

        var response = foodMenuService.getAllFoodMenus(1L, 0, "name");

        assertEquals(1, response.getBody().getData().getTotalElements());
    }

    @Test
    void getAllFoodMenus_shouldHandleEmptyPage() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findAllByRestaurant(eq(restaurant), any(Pageable.class))).thenReturn(Page.empty());

        var response = foodMenuService.getAllFoodMenus(1L, 0, "name");

        assertTrue(response.getBody().getData().isEmpty());
    }

    @Test
    void getAllFoodMenus_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RestaurantException.class, () -> foodMenuService.getAllFoodMenus(1L, 0, "name"));
    }

    // --- updateFoodMenu ---
    @Test
    void updateFoodMenu_shouldUpdateFieldsAndReturn() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.of(foodMenu));
        when(foodMenuRepository.save(any())).thenReturn(foodMenu);

        var update = new FoodMenuUpdateRequest("Updated Name", "Updated Desc", 30.0);
        var response = foodMenuService.updateFoodMenu(update, 1L, 10L);

        assertEquals("Updated Name", response.getBody().getData().name());
        assertEquals("Updated Desc", response.getBody().getData().description());
        assertEquals(30.0, response.getBody().getData().price());
    }

    @Test
    void updateFoodMenu_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        var update = new FoodMenuUpdateRequest("Name", "Desc", 10.0);
        assertThrows(RestaurantException.class, () -> foodMenuService.updateFoodMenu(update, 1L, 10L));
    }

    @Test
    void updateFoodMenu_shouldThrowIfMenuNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.empty());

        var update = new FoodMenuUpdateRequest("Name", "Desc", 10.0);
        assertThrows(RestaurantException.class, () -> foodMenuService.updateFoodMenu(update, 1L, 10L));
    }

    // --- deleteFoodMenu ---
    @Test
    void deleteFoodMenu_shouldDeleteSuccessfully() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.of(foodMenu));

        var response = foodMenuService.deleteFoodMenu(1L, 10L);

        assertEquals("Menu Deleted Successfully", response.getBody().getData());
        verify(foodMenuRepository, times(1)).delete(foodMenu);
    }

    @Test
    void deleteFoodMenu_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> foodMenuService.deleteFoodMenu(1L, 10L));
    }

    @Test
    void deleteFoodMenu_shouldThrowIfMenuNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodMenuRepository.findByIdAndRestaurant(10L, restaurant)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> foodMenuService.deleteFoodMenu(1L, 10L));
    }
}
