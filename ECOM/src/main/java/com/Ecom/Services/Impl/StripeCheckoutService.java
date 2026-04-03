package com.Ecom.Services.Impl;

import com.Ecom.Dtos.StripeCheckoutResponse;
import com.Ecom.Entities.Order;
import com.Ecom.Entities.OrderItem;
import com.Ecom.Repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StripeCheckoutService {

    private final OrderRepository orderRepository;

    @Value("${frontend.success.url}")
    private String successUrl;

    @Value("${frontend.cancel.url}")
    private String cancelUrl;

    public StripeCheckoutResponse createCheckoutSession(Long orderId) throws StripeException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order has no items");
        }

        BigDecimal totalAmount = calculateTotalAmount(order.getOrderItems());

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid total amount");
        }

        Long amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}&orderId=" + orderId)
                .setCancelUrl(cancelUrl + "?orderId=" + orderId)
                .setClientReferenceId(String.valueOf(orderId))
                .putMetadata("orderId", String.valueOf(orderId))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(amountInPaise)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order #" + orderId)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);

        return new StripeCheckoutResponse(
                order.getId(),
                session.getId(),
                session.getUrl(),
                order.getOrderStatus().name()
        );
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::calculateItemAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateItemAmount(OrderItem item) {

        double productPrice = item.getProduct().getPrice(); // <-- CHANGE THIS IF NEEDED

        return BigDecimal.valueOf(productPrice)
                .multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}