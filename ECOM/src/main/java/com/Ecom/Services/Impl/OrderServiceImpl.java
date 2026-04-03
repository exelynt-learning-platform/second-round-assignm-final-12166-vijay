package com.Ecom.Services.Impl;
import com.Ecom.Dtos.OrderDto;
import com.Ecom.Entities.Cart;
import com.Ecom.Entities.Enums.OrderStatus;
import com.Ecom.Entities.Order;
import com.Ecom.Entities.OrderItem;
import com.Ecom.Entities.User;
import com.Ecom.Exceptions.ResourceNotFoundException;
import com.Ecom.Repositories.CartRepository;
import com.Ecom.Repositories.OrderRepository;
import com.Ecom.Repositories.UserRepository;
import com.Ecom.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDto createOrder(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PLACED)
                .orderItems(new ArrayList<>())
                .totalAmount(BigDecimal.valueOf(cart.getTotalAmount()))
                .build();

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            return OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Clear Cart
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        cart.setCartTotalItems(0);
        cartRepository.save(cart);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getUserOrders(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }
}