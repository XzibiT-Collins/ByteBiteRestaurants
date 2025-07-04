package com.example.restaurant_service.utils;

import lombok.Getter;

@Getter
public enum RestaurantSort {
    SORT_BY_NAME("name");

    private final String field;
    RestaurantSort(String field){
        this.field=field;
    }
}
