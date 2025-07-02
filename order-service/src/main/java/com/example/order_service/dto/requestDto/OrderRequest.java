package com.example.order_service.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record OrderRequest(
        @NotNull(message = "Menu item id cannot be null")
        long menuItemId,

        @NotNull(message = "Restaurant id cannot be null")
        long restaurantId,

        @NotNull(message = "Customer id cannot be null")
        long customerId,

        @Size(min = 1, max = 100, message = "Quantity should be between 1 and 100")
        int quantity,

        @PositiveOrZero(message = "Price should be greater than or equal to zero")
        Double totalPrice
) {
}
