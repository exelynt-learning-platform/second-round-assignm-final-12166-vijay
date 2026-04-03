package com.Ecom.Services;

import com.Ecom.Dtos.CartDto;

public interface CartService {
    CartDto getCart(Integer userId);
    CartDto addToCart(Integer userId, Long productId, int quantity);
}