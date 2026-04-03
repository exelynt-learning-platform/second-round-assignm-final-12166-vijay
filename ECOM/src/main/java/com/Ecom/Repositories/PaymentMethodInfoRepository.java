package com.Ecom.Repositories;

import com.Ecom.Entities.PaymentMethodInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodInfoRepository extends JpaRepository<PaymentMethodInfo, Long> {
    Optional<PaymentMethodInfo> findByStripePaymentIntentId(String stripePaymentIntentId);
}