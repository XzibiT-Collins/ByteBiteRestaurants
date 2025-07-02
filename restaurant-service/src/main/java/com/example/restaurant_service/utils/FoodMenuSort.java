package com.example.restaurant_service.utils;

import lombok.Getter;

@Getter
public enum FoodMenuSort {
    SORT_BY_NAME("name"),
    SORT_BY_PRICE("price");

    private final String field;
    FoodMenuSort(String field){
        this.field=field;
    }
}
