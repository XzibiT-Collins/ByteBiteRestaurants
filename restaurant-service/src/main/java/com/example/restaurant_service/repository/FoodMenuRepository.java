package com.example.restaurant_service.repository;

import com.example.restaurant_service.model.FoodMenu;
import com.example.restaurant_service.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Long> {
    Page<FoodMenu> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

    @Query("SELECT f FROM FoodMenu f WHERE f.id = :id AND f.restaurant = :restaurant")
    Optional<FoodMenu> findByIdAndRestaurant(@Param("id") long id, @Param("restaurant") Restaurant restaurant);

}
