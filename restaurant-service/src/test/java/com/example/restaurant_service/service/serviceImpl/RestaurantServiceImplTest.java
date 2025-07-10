package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.feignClient.UserClient;
import com.example.restaurant_service.globalExceptionHandler.customExceptions.RestaurantException;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private RestaurantRequest restaurantRequest;

    @BeforeEach
    void setup() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Resto");
        restaurant.setLocation("Accra");
        restaurant.setEmail("test@resto.com");
        restaurant.setPhoneNumber("0241234567");
        restaurant.setOwnerId(101L);

        restaurantRequest = new RestaurantRequest(
                "Test Resto",
                "Accra",
                "0241234567",
                "test@resto.com",
                101L
        );
    }

    // --- addRestaurant ---
    @Test
    void addRestaurant_shouldSaveAndReturnResponse() {
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        var response = restaurantService.addRestaurant(restaurantRequest);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getStatus());
        assertEquals("Test Resto", response.getBody().getData().name());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void addRestaurant_shouldThrowExceptionIfRequestIsNull() {
        assertThrows(RestaurantException.class, () -> restaurantService.addRestaurant(null));
    }

    @Test
    void addRestaurant_shouldMapAllFieldsCorrectly() {
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));
        var response = restaurantService.addRestaurant(restaurantRequest);
        var result = response.getBody().getData();

        assertEquals("Accra", result.location());
        assertEquals("test@resto.com", result.email());
        assertEquals("0241234567", result.phoneNumber());
    }

    // --- getRestaurantById ---
    @Test
    void getRestaurantById_shouldReturnCorrectRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        var response = restaurantService.getRestaurantById(1L);

        assertEquals("Test Resto", response.getBody().getData().name());
        assertEquals("Accra", response.getBody().getData().location());
    }

    @Test
    void getRestaurantById_shouldThrowIfNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantService.getRestaurantById(99L));
    }

    @Test
    void getRestaurantById_shouldCallRepositoryOnce() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        restaurantService.getRestaurantById(1L);
        verify(restaurantRepository, times(1)).findById(1L);
    }

    // --- getAllRestaurants ---
    @Test
    void getAllRestaurants_shouldReturnPaginatedResponse() {
        Page<Restaurant> page = new PageImpl<>(List.of(restaurant));
        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(page);

        var response = restaurantService.getAllRestaurants(0, "name");

        assertEquals(1, response.getBody().getData().getTotalElements());
    }

    @Test
    void getAllRestaurants_shouldReturnEmptyPage() {
        when(restaurantRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        var response = restaurantService.getAllRestaurants(0, "name");

        assertTrue(response.getBody().getData().isEmpty());
    }

    // --- updateRestaurant ---
    @Test
    void updateRestaurant_shouldApplyChanges() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        var update = new RestaurantUpdateRequest("Updated Resto", "Kumasi", "0302999999", "updated@mail.com");

        var response = restaurantService.updateRestaurant(update, 1L);

        assertEquals("Updated Resto", response.getBody().getData().name());
    }

    @Test
    void updateRestaurant_shouldThrowIfRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        var update = new RestaurantUpdateRequest("Updated", null, null, null);
        assertThrows(RestaurantException.class, () -> restaurantService.updateRestaurant(update, 1L));
    }

    @Test
    void updateRestaurant_shouldIgnoreNullFields() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any())).thenReturn(restaurant);

        var update = new RestaurantUpdateRequest(null, null, null, null);

        var response = restaurantService.updateRestaurant(update, 1L);

        assertEquals("Test Resto", response.getBody().getData().name());
    }

    // --- deleteRestaurant ---
    @Test
    void deleteRestaurant_shouldDeleteSuccessfully() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        var response = restaurantService.deleteRestaurant(1L);

        assertEquals("Restaurant Deleted Successfully", response.getBody().getData());
    }

    @Test
    void deleteRestaurant_shouldThrowIfRestaurantMissing() {
        when(restaurantRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> restaurantService.deleteRestaurant(2L));
    }

    @Test
    void deleteRestaurant_shouldCallDeleteMethodOnce() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        restaurantService.deleteRestaurant(1L);
        verify(restaurantRepository, times(1)).delete(any(Restaurant.class));
    }
}
