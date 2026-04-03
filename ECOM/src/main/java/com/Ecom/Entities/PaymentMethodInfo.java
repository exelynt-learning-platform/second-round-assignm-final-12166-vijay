package com.Ecom.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment_method_info")
public class PaymentMethodInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String stripePaymentIntentId;

    @Column(length = 1000)
    private String clientSecret;

    private String stripeChargeId;

    private String currency;

    private Long amount;

    @OneToOne
    @JoinColumn(name ="payment_id", unique = true)
    private Payment payment;

}