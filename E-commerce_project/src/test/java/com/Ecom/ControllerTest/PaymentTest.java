package com.Ecom.ControllerTest;
import com.Ecom.Entities.Order;
import com.Ecom.Entities.OrderItem;
import com.Ecom.Entities.Product;
import com.Ecom.Repositories.OrderRepository;
import com.Ecom.Services.Impl.StripeCheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PaymentTest {

    private OrderRepository orderRepository;
    private StripeCheckoutService stripeCheckoutService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        stripeCheckoutService = new StripeCheckoutService(orderRepository);
    }

    @Test
    void testOrderNotFound() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            stripeCheckoutService.createCheckoutSession(1L);
        });
    }

    @Test
    void testOrderHasNoItems() throws Exception {
        Order order = new Order();
        order.setOrderItems(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> {
            stripeCheckoutService.createCheckoutSession(1L);
        });
    }

    @Test
    void testInvalidTotalAmount() throws Exception {
        Order order = new Order();

        Product product = new Product();
        product.setPrice(0.0);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);

        ArrayList<OrderItem> items = new ArrayList<>();
        items.add(item);

        order.setOrderItems(items);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> {
            stripeCheckoutService.createCheckoutSession(1L);
        });
    }
}