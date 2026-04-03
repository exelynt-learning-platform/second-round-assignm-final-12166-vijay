package com.Ecom.Services;

import com.Ecom.Dtos.CartDto;

public interface CartService {
    CartDto getCart(Integer userId);
    CartDto addToCart(Integer userId, Long productId, int quantity);
    CartDto updateCartItem(Integer userId, Long productId, int quantity);
    CartDto removeFromCart(Integer userId, Long productId);
    CartDto clearCart(Integer userId);
}