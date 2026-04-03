package com.Ecom.Services.Impl;
import com.Ecom.Dtos.CartDto;
import com.Ecom.Entities.Cart;
import com.Ecom.Entities.CartItem;
import com.Ecom.Entities.Product;
import com.Ecom.Entities.User;
import com.Ecom.Exceptions.ResourceNotFoundException;
import com.Ecom.Repositories.CartRepository;
import com.Ecom.Repositories.ProductRepository;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Services.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDto getCart(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    @Transactional
    public CartDto addToCart(Integer userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartItems(new ArrayList<>());
            newCart.setTotalAmount(0.0);
            newCart.setCartTotalItems(0);
            return newCart;
        });

        AtomicReference<Boolean> itemExists = new AtomicReference<>(false);

        cart.getCartItems().forEach(item -> {
            if (item.getProduct().equals(product)) {
                item.setQuantity(item.getQuantity() + quantity);
                itemExists.set(true);
            }
        });

        if (!itemExists.get()) {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getCartItems().add(cartItem);
        }


        double totalAmount = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        int totalItems = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        cart.setTotalAmount(totalAmount);
        cart.setCartTotalItems(totalItems);

        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }
}
