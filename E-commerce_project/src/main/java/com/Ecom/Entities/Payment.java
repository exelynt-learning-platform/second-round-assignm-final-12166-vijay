package com.Ecom.Entities;

import com.Ecom.Entities.Enums.PaymentMethod;
import com.Ecom.Entities.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;


    @Enumerated(EnumType.STRING)
    private PaymentStatus status=PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method=PaymentMethod.CARD;


    @OneToOne(mappedBy = "payment")
    private  PaymentMethodInfo paymentMethodInfo;




}