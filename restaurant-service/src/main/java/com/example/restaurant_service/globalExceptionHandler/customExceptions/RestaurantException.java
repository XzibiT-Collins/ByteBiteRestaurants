package com.example.restaurant_service.globalExceptionHandler.customExceptions;

public class RestaurantException extends RuntimeException {
    public RestaurantException(String message) {
        super(message);
    }
}
