package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.ApiResponseDto;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuRequest;
import com.example.restaurant_service.dto.foodMenuDto.requestDto.FoodMenuUpdateRequest;
import com.example.restaurant_service.dto.foodMenuDto.responseDto.FoodMenuResponse;
import com.example.restaurant_service.globalExceptionHandler.customExceptions.RestaurantException;
import com.example.restaurant_service.mapper.FoodMenuMapper;
import com.example.restaurant_service.model.FoodMenu;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.FoodMenuRepository;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.service.serviceInterfaces.FoodMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class FoodMenuServiceImpl implements FoodMenuService {
    private final FoodMenuRepository foodMenuRepository;
    private final RestaurantRepository restaurantRepository;

    public FoodMenuServiceImpl(FoodMenuRepository foodMenuRepository, RestaurantRepository restaurantRepository) {
        this.foodMenuRepository = foodMenuRepository;
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> addFoodMenu(long restaurantId, FoodMenuRequest foodMenuRequest) {
        if(foodMenuRequest != null){
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant Not Found with id: " + restaurantId));
            FoodMenu menu = FoodMenuMapper.toFoodMenu(foodMenuRequest);
            menu.setRestaurant(restaurant);
            log.info("Food Menu Saved Successfully with id: {} for restaurant with id: {}",menu.getId(),restaurant.getId());
            return ResponseEntity.ok(ApiResponseDto.success(FoodMenuMapper.toFoodMenuResponse(foodMenuRepository.save(menu)), HttpStatus.CREATED.value()));
        }else{
            log.info("Unable to save food menu with null request.");
            throw new RestaurantException("Food Menu Request is Null");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> getFoodMenuById(long id,long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant Not Found with id: " + restaurantId));
        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(id,restaurant).orElseThrow(() -> new RestaurantException("Menu item Not Found with id: " + id));
        log.info("Food Menu Found with id: {} for restaurant with id: {}",menu.getId(),restaurant.getId());
        return ResponseEntity.ok(ApiResponseDto.success(FoodMenuMapper.toFoodMenuResponse(menu), HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<Page<FoodMenuResponse>>> getAllFoodMenus(long restaurantId,int pageNumber, String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant Not Found with id: " + restaurantId));
        log.info("Fetching all food menus for restaurant with id: {} and sortField: {} ",restaurantId, sortField);
        return ResponseEntity.ok(ApiResponseDto.success(foodMenuRepository.findAllByRestaurant(restaurant,pageable).map(FoodMenuMapper::toFoodMenuResponse), HttpStatus.OK.value())) ;
    }

    @Override
    public ResponseEntity<ApiResponseDto<FoodMenuResponse>> updateFoodMenu(FoodMenuUpdateRequest foodMenuUpdateRequest, long restaurantId, long menuId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantException("Restaurant Not Found with id: " + restaurantId));

        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(menuId,restaurant).orElseThrow(() -> new RestaurantException("Menu item Not Found with id: " + menuId));

        if(foodMenuUpdateRequest.name() != null) menu.setName(foodMenuUpdateRequest.name());
        if(foodMenuUpdateRequest.description() != null) menu.setDescription(foodMenuUpdateRequest.description());
        if (foodMenuUpdateRequest.price() != null) menu.setPrice(foodMenuUpdateRequest.price());
        log.info("Food Menu Updated Successfully with id: {} for restaurant with id: {}",menu.getId(),restaurant.getId());
        return ResponseEntity.ok(ApiResponseDto.success(FoodMenuMapper.toFoodMenuResponse(foodMenuRepository.save(menu)),HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto<String>> deleteFoodMenu(long restaurantId, long id) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant Not Found with id: " + restaurantId));
        FoodMenu menu = foodMenuRepository.findByIdAndRestaurant(id,restaurant).orElseThrow(() -> new RuntimeException("Menu item Not Found with id: " + id));

        foodMenuRepository.delete(menu);
        log.info("Food Menu Deleted Successfully with id: {} for restaurant with id: {}",menu.getId(),restaurant.getId());
        return ResponseEntity.ok(ApiResponseDto.success("Menu Deleted Successfully", HttpStatus.OK.value()));
    }
}
