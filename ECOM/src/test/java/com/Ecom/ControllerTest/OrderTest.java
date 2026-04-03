package com.Ecom.ControllerTest;

import com.Ecom.Controllers.OrderController;
import com.Ecom.Dtos.OrderDto;
import com.Ecom.Services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderTest {

    @Test
    void testCreateOrder() {
        OrderService orderService = mock(OrderService.class);
        OrderController orderController = new OrderController(orderService);

        Map<String, Object> request = new HashMap<>();
        request.put("userId", 1);

        OrderDto orderDto = new OrderDto();

        when(orderService.createOrder(1)).thenReturn(orderDto);

        ResponseEntity<OrderDto> response = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetOrder() {
        OrderService orderService = mock(OrderService.class);
        OrderController orderController = new OrderController(orderService);

        Long orderId = 1L;
        OrderDto orderDto = new OrderDto();

        when(orderService.getOrder(orderId)).thenReturn(orderDto);

        ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUserOrders() {
        OrderService orderService = mock(OrderService.class);
        OrderController orderController = new OrderController(orderService);

        Integer userId = 1;
        List<OrderDto> orders = new ArrayList<>();
        orders.add(new OrderDto());

        when(orderService.getUserOrders(userId)).thenReturn(orders);

        ResponseEntity<List<OrderDto>> response = orderController.getUserOrders(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateOrder_UserIdMissing() {
        OrderService orderService = mock(OrderService.class);
        OrderController orderController = new OrderController(orderService);

        Map<String, Object> request = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            orderController.createOrder(request);
        });
    }
}
