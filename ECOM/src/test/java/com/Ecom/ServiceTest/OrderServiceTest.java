package com.Ecom.ServiceTest;
import com.Ecom.Dtos.OrderDto;
import com.Ecom.Entities.*;
import com.Ecom.Repositories.CartRepository;
import com.Ecom.Repositories.OrderRepository;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Services.Impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ModelMapper modelMapper = mock(ModelMapper.class);

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, userRepository, cartRepository, modelMapper);
    }

    @Test
    void testCreateOrder() {
        User user = new User();
        Product product = new Product();
        CartItem cartItem = CartItem.builder().product(product).quantity(2).build();
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        Order order = new Order();
        OrderDto orderDto = new OrderDto();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        assertNotNull(orderService.createOrder(1));
    }

    @Test
    void testGetOrder() {
        Order order = new Order();
        OrderDto orderDto = new OrderDto();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        assertNotNull(orderService.getOrder(1L));
    }

    @Test
    void testGetUserOrders() {
        User user = new User();
        Order order = new Order();
        OrderDto orderDto = new OrderDto();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        assertNotNull(orderService.getUserOrders(1));
    }
}