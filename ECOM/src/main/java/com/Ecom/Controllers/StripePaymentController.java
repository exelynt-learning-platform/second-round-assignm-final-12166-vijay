package com.Ecom.Controllers;

import com.Ecom.Dtos.StripeCheckoutResponse;
import com.Ecom.Services.Impl.StripeCheckoutService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripeCheckoutService stripeCheckoutService;

    @PostMapping("/stripe/{orderId}")
    public ResponseEntity<StripeCheckoutResponse> createCheckoutSession(@PathVariable Long orderId) throws StripeException {
        StripeCheckoutResponse response = stripeCheckoutService.createCheckoutSession(orderId);
        return ResponseEntity.ok(response);
    }
}