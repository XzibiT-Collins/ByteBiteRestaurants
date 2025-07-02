package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.mapper.FoodMenuMapper;
import com.example.restaurant_service.model.FoodMenu;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.FoodMenuRepository;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.service.serviceInterfaces.FoodMenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FoodMenuServiceImpl implements FoodMenuService {
    private final FoodMenuRepository foodMenuRepository;
    private final RestaurantRepository restaurantRepository;

    public FoodMenuServiceImpl(FoodMenuRepository foodMenuRepository, RestaurantRepository restaurantRepository) {
        this.foodMenuRepository = foodMenuRepository;
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    public ResponseEntity<FoodMenuResponse> addFoodMenu(long restaurantId,FoodMenuRequest foodMenuRequest) {
        if(foodMenuRequest != null){
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));
            FoodMenu menu = FoodMenuMapper.toFoodMenu(foodMenuRequest);
            menu.setRestaurant(restaurant);
            return ResponseEntity.ok(FoodMenuMapper.toFoodMenuResponse(foodMenuRepository.save(menu)));
        }else{
            throw new RuntimeException("Food Menu Request is Null");
        }
    }

    @Override
    public ResponseEntity<FoodMenuResponse> getFoodMenuById(long id,long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));
        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(id,restaurant).orElseThrow(() -> new RuntimeException("Menu item Not Found with id: " + id));
        return ResponseEntity.ok(FoodMenuMapper.toFoodMenuResponse(menu));
    }

    @Override
    public ResponseEntity<Page<FoodMenuResponse>> getAllFoodMenus(long restaurantId,int pageNumber, String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));

        return ResponseEntity.ok(foodMenuRepository.findAllByRestaurant(restaurant,pageable).map(FoodMenuMapper::toFoodMenuResponse)) ;
    }

    @Override
    public ResponseEntity<FoodMenuResponse> updateFoodMenu(FoodMenuUpdateRequest foodMenuUpdateRequest, long restaurantId, long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));

        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(menuId,restaurant).orElseThrow(() -> new RuntimeException("Menu item Not Found with id: " + menuId));

        if(foodMenuUpdateRequest.name() != null) menu.setName(foodMenuUpdateRequest.name());
        if(foodMenuUpdateRequest.description() != null) menu.setDescription(foodMenuUpdateRequest.description());
        if (foodMenuUpdateRequest.price() != null) menu.setPrice(foodMenuUpdateRequest.price());

        return ResponseEntity.ok(FoodMenuMapper.toFoodMenuResponse(foodMenuRepository.save(menu)));
    }

    @Override
    public ResponseEntity<String> deleteFoodMenu(long restaurantId, long id) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));
        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(id,restaurant).orElseThrow(() -> new RuntimeException("Menu item Not Found with id: " + id));

        foodMenuRepository.delete(menu);
        return ResponseEntity.ok("Menu Deleted Successfully");
    }
}
