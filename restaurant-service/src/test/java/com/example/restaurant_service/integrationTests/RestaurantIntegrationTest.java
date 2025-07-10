package com.example.restaurant_service.integrationTests;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.security.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.kafka.bootstrap-servers=localhost:9092"
        })
@AutoConfigureMockMvc
@Testcontainers
public class RestaurantIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        var userDetails = new org.springframework.security.core.userdetails.User(
                "owner@example.com", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_RESTAURANT_OWNER"))
        );
        jwtToken = jwtService.generateToken(userDetails);

        restaurant = Restaurant.builder()
                .name("Papaye")
                .location("Kumasi")
                .email("contact@papaye.com")
                .phoneNumber("0244556666")
                .ownerId(1L)
                .build();
    }

    @AfterEach
    @Transactional
    void cleanUp() {
        restaurantRepository.deleteAll();
    }

    @Test
    void shouldCreateRestaurant() throws Exception {
        RestaurantRequest request = new RestaurantRequest(
                "ChopBar",
                "Accra",
                "0550001111",
                "owner@example.com",
                1L
        );

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("ChopBar"));
    }

    @Test
    void shouldGetRestaurantById() throws Exception {

        Restaurant saved = restaurantRepository.save(restaurant);

        mockMvc.perform(get("/api/v1/restaurant/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Papaye"));
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        Restaurant saved = restaurantRepository.save(restaurant);

        var update = new com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest(
                "Updated", "Newtown", "b@b.com", "0556667777"
        );

        mockMvc.perform(patch("/api/v1/restaurant/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated"));
    }

    @Test
    void shouldListRestaurants() throws Exception {
        Restaurant restaurantOne = Restaurant.builder()
                .name("Papaye")
                .location("Kumasi")
                .email("contact@papaye.com")
                .phoneNumber("0244556666")
                .ownerId(1L)
                .build();

        Restaurant restaurantTwo = Restaurant.builder()
                .name("marwako")
                .location("Accra")
                .email("contact@marwako.com")
                .phoneNumber("0244556666")
                .ownerId(1L)
                .build();
        restaurantRepository.save(restaurantOne);
        restaurantRepository.save(restaurantTwo);

        mockMvc.perform(get("/api/v1/restaurant/")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        Restaurant restaurantToDelete = Restaurant.builder()
                .name("Papaye")
                .location("Kumasi")
                .email("contact@papaye.com")
                .phoneNumber("0244556666")
                .ownerId(1L)
                .build();
        Restaurant saved = restaurantRepository.save(restaurantToDelete);

        mockMvc.perform(delete("/api/v1/restaurant/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Restaurant Deleted Successfully"));
    }

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/restaurant"))
                .andExpect(status().isForbidden());
    }
}
