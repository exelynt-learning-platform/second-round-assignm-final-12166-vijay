package com.Ecom.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponse {
    private Long orderId;
    private String paymentIntentId;
    private String clientSecret;
    private String publishableKey;
    private String paymentStatus;


}