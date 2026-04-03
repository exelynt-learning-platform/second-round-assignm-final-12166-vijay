package com.Ecom.ControllerTest;

import com.Ecom.Controllers.CartController;
import com.Ecom.Dtos.CartDto;
import com.Ecom.Services.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CartTest {

    @Test
    void testGetCart() {
        CartService cartService = mock(CartService.class);
        CartController cartController = new CartController(cartService);

        Integer userId = 1;
        CartDto cartDto = new CartDto();

        when(cartService.getCart(userId)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.getCart(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddToCart() {
        CartService cartService = mock(CartService.class);
        CartController cartController = new CartController(cartService);

        Integer userId = 1;
        Long productId = 101L;
        Integer quantity = 2;

        Map<String, Object> request = new HashMap<>();
        request.put("productId", 101);
        request.put("quantity", 2);

        CartDto cartDto = new CartDto();

        when(cartService.addToCart(userId, productId, quantity)).thenReturn(cartDto);

        ResponseEntity<CartDto> response = cartController.addToCart(userId, request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());


        System.out.println("Test passed...!!!");
    }
}