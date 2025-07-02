package com.example.restaurant_service.service.serviceImpl;

import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantRequest;
import com.example.restaurant_service.dto.restaurantDto.requestDto.RestaurantUpdateRequest;
import com.example.restaurant_service.dto.restaurantDto.responseDto.RestaurantResponse;
import com.example.restaurant_service.mapper.RestaurantMapper;
import com.example.restaurant_service.model.Restaurant;
import com.example.restaurant_service.repository.RestaurantRepository;
import com.example.restaurant_service.service.serviceInterfaces.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    public ResponseEntity<Restaurant> addRestaurant(RestaurantRequest restaurantRequest) {
        if(restaurantRequest != null){
            return ResponseEntity.ok(restaurantRepository.save(RestaurantMapper.toRestaurant(restaurantRequest)));
        }else{
            throw new RuntimeException("Restaurant Request is Null");
        }
    }

    @Override
    public ResponseEntity<RestaurantResponse> getRestaurantById(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurant Not Found with id: " + id));

        return ResponseEntity.ok(RestaurantMapper.toRestaurantResponse(restaurant));
    }

    @Override
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(int pageNumber,String sortField) {
        int paginateBy = 10;
        Sort sort = Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, paginateBy, sort);

        return ResponseEntity.ok(restaurantRepository.findAll(pageable).map(RestaurantMapper::toRestaurantResponse));
    }

    @Override
    public ResponseEntity<RestaurantResponse> updateRestaurant(RestaurantUpdateRequest restaurantUpdateRequest, long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurant Not Found with id: " + id));

        if(restaurantUpdateRequest.name() != null) restaurant.setName(restaurantUpdateRequest.name());
        if(restaurantUpdateRequest.location() != null) restaurant.setLocation(restaurantUpdateRequest.location());
        if(restaurantUpdateRequest.email() != null) restaurant.setEmail(restaurantUpdateRequest.email());
        if(restaurantUpdateRequest.phoneNumber() != null) restaurant.setPhoneNumber(restaurantUpdateRequest.phoneNumber());

        return ResponseEntity.ok(RestaurantMapper.toRestaurantResponse(restaurantRepository.save(restaurant)));
    }

    @Override
    public ResponseEntity<String> deleteRestaurant(long id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurant Not Found with id: " + id));
        restaurantRepository.delete(restaurant);
        return ResponseEntity.ok("Restaurant Deleted Successfully");
    }
}
