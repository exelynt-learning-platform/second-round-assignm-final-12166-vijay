package com.Ecom.Dtos;

import com.Ecom.Entities.Enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    @NotNull(message = "User is required")
    @Valid
    private UserDto user;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemDto> orderItems = new ArrayList<>();
}