package com.Ecom.Controllers;

import com.Ecom.Dtos.CartDto;
import com.Ecom.Services.CartService;
import lombok.RequiredArgsConstructor;
import com.Ecom.Dtos.Requests.AddToCartRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Integer userId) {
        CartDto cartDto = cartService.getCart(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addToCart(@PathVariable Integer userId, @Valid @RequestBody AddToCartRequest request) {
        CartDto cartDto = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable Integer userId, @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, request.getProductId(), request.getQuantity()));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDto> removeFromCart(@PathVariable Integer userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CartDto> clearCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}