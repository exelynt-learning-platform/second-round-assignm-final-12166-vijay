package com.Ecom.ServiceTest;

import com.Ecom.Dtos.CartDto;
import com.Ecom.Entities.Cart;
import com.Ecom.Entities.Product;
import com.Ecom.Entities.User;
import com.Ecom.Repositories.CartRepository;
import com.Ecom.Repositories.ProductRepository;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Services.Impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private CartRepository cartRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        modelMapper = mock(ModelMapper.class);

        cartService = new CartServiceImpl(cartRepository, userRepository, productRepository, modelMapper);
    }

    @Test
    void testGetCart() {
        Integer userId = 1;
        User user = new User();
        Cart cart = new Cart();
        CartDto cartDto = new CartDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(modelMapper.map(cart, CartDto.class)).thenReturn(cartDto);

        CartDto result = cartService.getCart(userId);

        assertNotNull(result);
    }

    @Test
    void testAddToCart() {
        Integer userId = 1;
        Long productId = 1L;

        User user = new User();
        Product product = new Product();
        product.setPrice(100.0);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());

        CartDto cartDto = new CartDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(modelMapper.map(cart, CartDto.class)).thenReturn(cartDto);

        CartDto result = cartService.addToCart(userId, productId, 2);

        assertNotNull(result);
    }
}