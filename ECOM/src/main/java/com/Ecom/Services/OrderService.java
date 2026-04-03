package com.Ecom.Services;

import com.Ecom.Dtos.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Integer userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Integer userId);
}