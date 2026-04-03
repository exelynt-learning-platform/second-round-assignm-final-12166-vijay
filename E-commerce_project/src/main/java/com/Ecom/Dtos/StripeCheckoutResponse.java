package com.Ecom.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeCheckoutResponse {
    private Long orderId;
    private String sessionId;
    private String paymentUrl;
    private String status;
}