package com.example.restaurant_service.utils;

import com.example.restaurant_service.repository.FoodMenuRepository;
import com.example.restaurant_service.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

@Component("resourceOwner")
public class ResourceOwnership {
    private final RestaurantRepository restaurantRepository;
    private final FoodMenuRepository foodMenuRepository;

    public ResourceOwnership(RestaurantRepository restaurantRepository,
                             FoodMenuRepository foodMenuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.foodMenuRepository = foodMenuRepository;
    }

    public boolean isRestaurantOwner(long objectId, long userId) {
        return restaurantRepository.findById(objectId)
                .map(restaurant -> restaurant.getOwnerId() == userId)
                .orElse(false);
    }

    public boolean isFoodMenuOwner(long objectId, long userId) {
        return foodMenuRepository.findById(objectId)
                .map(foodMenu -> foodMenu.getRestaurant().getId() == userId)
                .orElse(false);
    }
}
