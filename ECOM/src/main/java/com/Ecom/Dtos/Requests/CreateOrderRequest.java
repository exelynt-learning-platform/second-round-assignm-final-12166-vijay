package com.Ecom.Dtos.Requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequest {
    @NotNull(message = "userId is required")
    private Integer userId;
}
